package colfume.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SearchCondition {

    LATEST("최신순"),
    ACCURACY("정확도순");

    private final String description;
}