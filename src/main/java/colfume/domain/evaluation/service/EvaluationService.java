package colfume.domain.evaluation.service;

import colfume.api.dto.evaluation.EvaluationRequestDto;
import colfume.common.converter.entity.EvaluationConverter;
import colfume.common.enums.ErrorCode;
import colfume.domain.evaluation.model.entity.Evaluation;
import colfume.domain.evaluation.model.repository.EvaluationRepository;
import colfume.domain.evaluation.service.exception.CrudNotAuthenticationException;
import colfume.domain.member.model.entity.Member;
import colfume.domain.member.model.repository.MemberRepository;
import colfume.domain.perfume.model.entity.Perfume;
import colfume.domain.perfume.model.repository.PerfumeRepository;
import colfume.domain.perfume.service.exception.PerfumeNotFoundException;
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

    @Transactional
    public Long createEvaluation(EvaluationRequestDto evaluationRequestDto, Long writerId, Long perfumeId) {
        Member writer = memberRepository.getReferenceById(writerId);
        Perfume perfume = perfumeRepository.findById(perfumeId)
                .orElseThrow(() -> new PerfumeNotFoundException(ErrorCode.PERFUME_NOT_FOUND));

        converter.update(writer, perfume);
        Evaluation evaluation = converter.convertToEntity(evaluationRequestDto);

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
    public void deleteEvaluation(Long evaluationId, Long userId) {
        Evaluation evaluation = findEvaluation(evaluationId);
        if (!evaluation.getWriter().getId().equals(userId)) {
            throw new CrudNotAuthenticationException(ErrorCode.NOT_AUTHENTICATED);
        }
        Perfume perfume = evaluation.getPerfume();
        evaluation.delete(); // soft delete
        perfume.subtractEvaluationCount(); // 평가수 -1
        perfumeRepository.updateScoreForSubtract(perfume.getId(), evaluation.getScore()); // 평가 점수 update (삭제)
    }

    private Evaluation findEvaluation(Long evaluationId) {
        return evaluationRepository.findById(evaluationId)
                .orElseThrow();
    }
}