package colfume.domain.perfume.model.entity;

import colfume.common.enums.ColorType;

import javax.persistence.AttributeConverter;
import java.util.Arrays;

public class ColorTypeConverter implements AttributeConverter<ColorType, String> {

    @Override
    public String convertToDatabaseColumn(ColorType colorType) {
        return colorType.getValue();
    }

    @Override
    public ColorType convertToEntityAttribute(String value) {
        return Arrays.stream(ColorType.values())
                .filter(colorType -> colorType.getValue().equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("찾으려는 색깔 타입이 존재하지 않습니다."));
    }
}