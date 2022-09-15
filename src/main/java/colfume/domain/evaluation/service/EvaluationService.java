package colfume.domain.evaluation.service;

import colfume.domain.evaluation.model.entity.Evaluation;
import colfume.domain.evaluation.model.repository.EvaluationRepository;
import colfume.domain.member.model.entity.Member;
import colfume.domain.member.model.repository.MemberRepository;
import colfume.domain.perfume.model.entity.Perfume;
import colfume.domain.perfume.model.repository.PerfumeRepository;
import colfume.enums.ErrorCode;
import colfume.exception.PerfumeNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static colfume.dto.EvaluationDto.*;

@Service
@Transactional
@RequiredArgsConstructor
public class EvaluationService {

    private final EvaluationRepository evaluationRepository;
    private final MemberRepository memberRepository;
    private final PerfumeRepository perfumeRepository;

    public Long createEvaluation(EvaluationRequestDto evaluationRequestDto, Long writerId, Long perfumeId) {
        Member writer = memberRepository.getReferenceById(writerId);
        Perfume perfume = perfumeRepository.findById(perfumeId)
                .orElseThrow(() -> new PerfumeNotFoundException(ErrorCode.PERFUME_NOT_FOUND));
        Evaluation evaluation = Evaluation.builder()
                .writer(writer)
                .perfume(perfume)
                .content(evaluationRequestDto.getContent())
                .build();

        return evaluationRepository.save(evaluation).getId();
    }

    public void updateContent(Long evaluationId, Long userId, String content) {
        Evaluation evaluation = findEvaluation(evaluationId);
        if (!evaluation.getWriter().getId().equals(userId)) {
            throw new IllegalStateException("수정 권한이 없습니다.");
        }
        evaluation.updateContent(content);
    }

    public void deleteEvaluation(Long evaluationId, Long userId) {
        Evaluation evaluation = findEvaluation(evaluationId);
        if (!evaluation.getWriter().getId().equals(userId)) {
            throw new IllegalStateException("삭제 권한이 없습니다.");
        }
        evaluationRepository.delete(evaluation);
    }

    @Transactional(readOnly = true)
    public List<EvaluationResponseDto> findEvaluationDtoListByPerfumeId(Long perfumeId) {
        Perfume perfume = perfumeRepository.findById(perfumeId)
                .orElseThrow(() -> new PerfumeNotFoundException(ErrorCode.PERFUME_NOT_FOUND));

        return evaluationRepository.findByPerfume(perfume).stream()
                .map(EvaluationResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    private Evaluation findEvaluation(Long evaluationId) {
        return evaluationRepository.findById(evaluationId)
                .orElseThrow();
    }
}