package colfume.common.converter.enums;

import colfume.common.enums.ColorType;

import java.util.Arrays;

public class ColorTypeConverter implements EnumConverter<ColorType, String> {

    @Override
    public ColorType convertToEnum(String value) {
        return Arrays.stream(ColorType.values())
                .filter(colorType -> colorType.getValue().equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("찾으려는 색깔 타입이 존재하지 않습니다."));
    }

    @Override
    public String convertToValue(ColorType colorType) {
        return colorType.getValue();
    }
}