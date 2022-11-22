package colfume.domain.bookmark.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookmarkRequestDto {

    private Long userId;
    private Long perfumeId;
    private String redirectUrl;
}