package colfume.common.converter.entity;

import colfume.api.dto.evaluation.EvaluationRequestDto;
import colfume.domain.evaluation.model.entity.Evaluation;
import colfume.domain.evaluation.service.dto.EvaluationResponseDto;
import colfume.domain.member.model.entity.Member;
import colfume.domain.perfume.model.entity.Perfume;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EvaluationConverter implements EntityConverter<Evaluation, EvaluationRequestDto, EvaluationResponseDto> {

    private final Member writer;
    private final Perfume perfume;

    @Override
    public Evaluation convertToEntity(EvaluationRequestDto evaluationRequestDto) {
        return Evaluation.builder()
                .writer(writer)
                .perfume(perfume)
                .content(evaluationRequestDto.getContent())
                .score(evaluationRequestDto.getScore())
                .build();
    }

    @Override
    public EvaluationResponseDto convertToDto(Evaluation evaluation) {
        return new EvaluationResponseDto(
                evaluation.getId(),
                evaluation.getWriter().getId(),
                evaluation.getPerfume().getId(),
                evaluation.getContent(),
                evaluation.getScore(),
                evaluation.isDeleted(),
                evaluation.getCreatedDate(),
                evaluation.getLastModifiedDate()
        );
    }
}