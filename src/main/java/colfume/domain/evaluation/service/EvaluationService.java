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

    @Transactional
    public Long createEvaluation(EvaluationRequestDto evaluationRequestDto, Long writerId, Long perfumeId) {
        Member writer = memberRepository.getReferenceById(writerId);
        Perfume perfume = perfumeRepository.findById(perfumeId)
                .orElseThrow(() -> new PerfumeNotFoundException(ErrorCode.PERFUME_NOT_FOUND));

        EvaluationConverter evaluationConverter = new EvaluationConverter(writer, perfume);
        Evaluation evaluation = evaluationConverter.convertToEntity(evaluationRequestDto);

        perfume.addEvaluationCount(); // 평가수 +1
        perfumeRepository.updateScoreForAdd(perfume.getId(), evaluationRequestDto.getScore()); // 평가 점수 update (추가)

        return evaluationRepository.save(evaluation).getId();
    }

    @Transactional
    public void update(Long evaluationId, Long userId, String content, int score) {
        Evaluation evaluation = findEvaluation(evaluationId);
        if (!evaluation.getWriter().getId().equals(userId)) {
            throw new CrudNotAuthenticationException(ErrorCode.NOT_AUTHENTICATED);
        }
        evaluation.update(content, score);
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