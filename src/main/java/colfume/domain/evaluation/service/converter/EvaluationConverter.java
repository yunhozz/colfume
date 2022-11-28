package colfume.domain.evaluation.service.converter;

import colfume.common.EntityConverter;
import colfume.domain.evaluation.dto.request.EvaluationRequestDto;
import colfume.domain.evaluation.dto.response.EvaluationResponseDto;
import colfume.domain.evaluation.model.entity.Evaluation;
import colfume.domain.member.model.entity.Member;
import colfume.domain.perfume.model.entity.Perfume;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class EvaluationConverter implements EntityConverter<Evaluation, EvaluationRequestDto, EvaluationResponseDto> {

    private Member writer;
    private Perfume perfume;

    public EvaluationConverter(Member writer, Perfume perfume) {
        this.writer = writer;
        this.perfume = perfume;
    }

    @Override
    public Evaluation convertToEntity(EvaluationRequestDto evaluationRequestDto) {
        if (writer == null || perfume == null) {
            throw new IllegalStateException("연관된 엔티티가 생성되지 않았습니다.");
        }

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