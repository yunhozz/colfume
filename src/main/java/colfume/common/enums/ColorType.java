package colfume.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ColorType {

    RED("빨간색"),
    ORANGE("주황색"),
    YELLOW("노란색"),
    GREEN("초록색");

    private final String value;
}