package colfume.domain.evaluation.service.converter;

import colfume.common.EntityConverter;
import colfume.domain.evaluation.dto.request.EvaluationRequestDto;
import colfume.domain.evaluation.dto.response.EvaluationResponseDto;
import colfume.domain.evaluation.model.entity.Evaluation;
import colfume.domain.member.model.entity.Member;
import colfume.domain.perfume.model.entity.Perfume;
import org.springframework.stereotype.Component;

@Component
public class EvaluationConverter implements EntityConverter<Evaluation, EvaluationRequestDto, EvaluationResponseDto> {

    private Member writer;
    private Perfume perfume;

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

    public void setEntities(Member writer, Perfume perfume) {
        this.writer = writer;
        this.perfume = perfume;
    }
}