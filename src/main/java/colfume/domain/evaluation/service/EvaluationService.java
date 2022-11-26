package colfume.domain.evaluation.service;

import colfume.domain.evaluation.dto.request.EvaluationRequestDto;
import colfume.domain.evaluation.model.entity.Evaluation;
import colfume.domain.evaluation.model.repository.EvaluationRepository;
import colfume.domain.evaluation.service.converter.EvaluationConverter;
import colfume.domain.evaluation.service.exception.CrudNotAuthenticationException;
import colfume.domain.evaluation.service.exception.EvaluationAlreadyDeletedException;
import colfume.domain.evaluation.service.exception.EvaluationAlreadyExistException;
import colfume.domain.member.model.entity.Member;
import colfume.domain.member.model.repository.MemberRepository;
import colfume.domain.perfume.model.entity.Perfume;
import colfume.domain.perfume.model.repository.PerfumeRepository;
import colfume.domain.perfume.service.PerfumeNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EvaluationService {

    private final EvaluationRepository evaluationRepository;
    private final MemberRepository memberRepository;
    private final PerfumeRepository perfumeRepository;
    private final EvaluationConverter converter;

    /**
     * 향수에 대한 평가가 이미 존재할 때 : 삭제된 상태면 create 가능, 아니면 예외 발생
     * 향수에 대한 평가가 없을 때 : 평가를 새로 생성 후 레포지토리에 저장
     */
    @Transactional
    public Long createEvaluation(EvaluationRequestDto evaluationRequestDto, Long writerId, Long perfumeId) {
        Member writer = memberRepository.getReferenceById(writerId);
        Perfume perfume = perfumeRepository.findById(perfumeId).orElseThrow(PerfumeNotFoundException::new);
        final Long[] id = {null};

        evaluationRepository.findByWriterAndPerfume(writer, perfume)
                .ifPresentOrElse(evaluation -> {
                    if (evaluation.isDeleted()) {
                        id[0] = saveEvaluation(writer, perfume, evaluationRequestDto);

                    } else throw new EvaluationAlreadyExistException();

                }, () -> {
                    id[0] = saveEvaluation(writer, perfume, evaluationRequestDto);
                });

        perfume.updateScoreForAdd(evaluationRequestDto.getScore());

        return id[0];
    }

    @Transactional
    public void update(Long evaluationId, Long userId, EvaluationRequestDto evaluationRequestDto) {
        Evaluation evaluation = validateAuthorization(evaluationId, userId);
        evaluation.update(evaluationRequestDto.getContent(), evaluationRequestDto.getScore()); // perfume.updateScoreForModify()
//        perfumeRepository.updateScoreForModify(perfume.getId(), oldScore, evaluationRequestDto.getScore()); // 평가 점수 update (수정)

        /*
         * 벌크성 쿼리의 clearAutomatically = true 옵션에 의해 영속성 컨텍스트를 무시하고 db 에 바로 업데이트 쿼리를 날리고 1차 캐시를 초기화
         * 트랜잭션이 끝나고 1차 캐시에 남아있던 evaluation 의 update 쿼리가 commit 되지 못하고 무시됨
         * 따라서, flushAutomatically = true 를 추가적으로 선언하여 모든 변경 내용을 강제로 flush 하게 만듬
         */
    }

    @Transactional
    public void delete(Long evaluationId, Long userId) {
        Evaluation evaluation = validateAuthorization(evaluationId, userId);
        evaluation.delete(); // perfume.updateScoreForSubtract()
//        perfumeRepository.updateScoreForSubtract(perfume.getId(), evaluation.getScore()); // 평가 점수 update (삭제)

        /*
         * 원래대로라면 벌크성 쿼리에 의해 evaluation.delete() 가 무시되는 것이 맞음
         * 하지만, delete() 메소드 안의 perfume.subtractEvaluationCount() 이 실행되어야 하기 때문에 evaluation 의 변경내용이 무시되지 못함
         */
    }

    private Long saveEvaluation(Member writer, Perfume perfume, EvaluationRequestDto evaluationRequestDto) {
        converter.setEntities(writer, perfume);
        Evaluation evaluation = converter.convertToEntity(evaluationRequestDto);
        evaluationRepository.save(evaluation);

        return evaluation.getId();
    }

    private Evaluation validateAuthorization(Long evaluationId, Long userId) {
        Evaluation evaluation = evaluationRepository.findWithPerfumeByIdAndUserId(evaluationId, userId)
                .orElseThrow(CrudNotAuthenticationException::new);

        if (evaluation.isDeleted()) {
            throw new EvaluationAlreadyDeletedException();
        }

        return evaluation;
    }
}