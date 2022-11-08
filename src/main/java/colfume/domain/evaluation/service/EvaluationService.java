package colfume.domain.evaluation.service;

import colfume.api.dto.evaluation.EvaluationRequestDto;
import colfume.common.converter.entity.EvaluationConverter;
import colfume.common.enums.ErrorCode;
import colfume.domain.evaluation.model.entity.Evaluation;
import colfume.domain.evaluation.model.repository.EvaluationRepository;
import colfume.domain.evaluation.service.exception.CrudNotAuthenticationException;
import colfume.domain.evaluation.service.exception.EvaluationAlreadyExistException;
import colfume.domain.member.model.entity.Member;
import colfume.domain.member.model.repository.MemberRepository;
import colfume.domain.perfume.model.entity.Perfume;
import colfume.domain.perfume.model.repository.PerfumeRepository;
import colfume.domain.perfume.service.exception.PerfumeNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EvaluationService {

    private final EvaluationRepository evaluationRepository;
    private final MemberRepository memberRepository;
    private final PerfumeRepository perfumeRepository;
    private final EvaluationConverter converter;

    @Transactional
    public Long createEvaluation(EvaluationRequestDto evaluationRequestDto, Long writerId, Long perfumeId) {
        Member writer = memberRepository.getReferenceById(writerId);
        Perfume perfume = perfumeRepository.findById(perfumeId)
                .orElseThrow(() -> new PerfumeNotFoundException(ErrorCode.PERFUME_NOT_FOUND));

        Evaluation evaluation = validateAndSaveEvaluation(writer, perfume, evaluationRequestDto);
        perfume.addEvaluationCount(); // 평가수 +1
        perfumeRepository.updateScoreForAdd(perfume.getId(), evaluationRequestDto.getScore()); // 평가 점수 update (추가)

        return evaluationRepository.save(evaluation).getId();
    }

    @Transactional
    public void update(Long evaluationId, Long userId, EvaluationRequestDto evaluationRequestDto) {
        Evaluation evaluation = validateAuthorization(evaluationId, userId);
        Perfume perfume = evaluation.getPerfume();
        double oldScore = evaluation.getScore();

        /*
         * clearAutomatically = true 만 선언했을 때, 해당 JPQL 과 관련된 엔티티들만 flush 함 -> evaluation 에 대한 update 쿼리는 나가지 않음
         * 따라서, flushAutomatically = true 를 추가적으로 선언하여 모든 변경 내용을 다 flush 하게 만듬
         */
        evaluation.update(evaluationRequestDto.getContent(), evaluationRequestDto.getScore());
        perfumeRepository.updateScoreForModify(perfume.getId(), oldScore, evaluationRequestDto.getScore()); // 평가 점수 update (수정)
    }

    @Transactional
    public void delete(Long evaluationId, Long userId) {
        Evaluation evaluation = validateAuthorization(evaluationId, userId);
        Perfume perfume = evaluation.getPerfume();
        evaluation.delete(); // perfume.subtractEvaluationCount()

        if (perfume.getEvaluationCount() == 0) {
            perfume.scoreToZero();

        } else {
            perfumeRepository.updateScoreForSubtract(perfume.getId(), evaluation.getScore()); // 평가 점수 update (삭제)
        }
    }

    /**
     * 향수에 대한 평가가 이미 존재할 때 : 삭제된 상태면 create 상태로 변경 & update, 아니면 예외 발생
     * 향수에 대한 평가가 없을 때 : 평가를 새로 생성 후 레포지토리에 저장
     */
    private Evaluation validateAndSaveEvaluation(Member writer, Perfume perfume, EvaluationRequestDto evaluationRequestDto) {
        Optional<Evaluation> optionalEvaluation = evaluationRepository.findByWriterAndPerfume(writer, perfume);
        Evaluation evaluation;

        if (optionalEvaluation.isPresent()) {
            evaluation = optionalEvaluation.get();

            if (evaluation.isDeleted()) {
                evaluation.updateAfterDeleted(evaluationRequestDto.getContent(), evaluationRequestDto.getScore());

            } else throw new EvaluationAlreadyExistException(ErrorCode.EVALUATION_ALREADY_EXIST);

        } else {
            converter.update(writer, perfume);
            evaluation = converter.convertToEntity(evaluationRequestDto);
            evaluationRepository.save(evaluation);
        }
        return evaluation;
    }

    private Evaluation validateAuthorization(Long evaluationId, Long userId) {
        Optional<Evaluation> optionalEvaluation = evaluationRepository.findWithPerfumeByIdAndUserId(evaluationId, userId);

        if (optionalEvaluation.isEmpty()) {
            throw new CrudNotAuthenticationException(ErrorCode.NOT_AUTHENTICATED);
        }
        return optionalEvaluation.get();
    }
}