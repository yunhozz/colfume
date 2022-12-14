package colfume.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SortCondition {

    LATEST("최신순"),
    POPULARITY("인기순");

    private final String description;
}