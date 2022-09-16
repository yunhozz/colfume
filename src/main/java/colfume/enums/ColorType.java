package colfume.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ColorType {

    RED("COLOR_RED", "빨간색"),
    ORANGE("COLOR_ORANGE", "주황색"),
    YELLOW("COLOR_YELLOW", "노란색"),
    GREEN("COLOR_GREEN", "초록색");

    private final String key;
    private final String value;
}
