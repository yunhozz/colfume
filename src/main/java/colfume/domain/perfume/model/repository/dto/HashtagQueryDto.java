package colfume.domain.perfume.model.repository.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class HashtagQueryDto {

    private Long id;
    private Long perfumeId;
    private String tag;

    @QueryProjection
    public HashtagQueryDto(Long id, Long perfumeId, String tag) {
        this.id = id;
        this.perfumeId = perfumeId;
        this.tag = tag;
    }
}