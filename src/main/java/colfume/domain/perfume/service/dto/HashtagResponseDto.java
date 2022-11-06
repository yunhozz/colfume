package colfume.domain.perfume.service.dto;

import colfume.domain.perfume.model.entity.Hashtag;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class HashtagResponseDto {

    private Long id;
    private String tag;

    public HashtagResponseDto(Hashtag hashtag) {
        id = hashtag.getId();
        tag = hashtag.getTag();
    }
}