package colfume.domain.perfume.model.repository.dto;

import colfume.domain.perfume.model.entity.Hashtag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HashtagResponseDto {

    private Long id;
    private Long perfumeId;
    private String tag;

    public HashtagResponseDto(Hashtag hashtag) {
        id = hashtag.getId();
        perfumeId = hashtag.getPerfume().getId();
        tag = hashtag.getTag();
    }
}