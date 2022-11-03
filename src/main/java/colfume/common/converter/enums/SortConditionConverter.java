package colfume.common.converter.enums;

import colfume.common.enums.SortCondition;

import javax.persistence.AttributeConverter;
import java.util.Arrays;

public class SortConditionConverter implements AttributeConverter<SortCondition, String> {

    @Override
    public String convertToDatabaseColumn(SortCondition sortCondition) {
        return sortCondition.getDescription();
    }

    @Override
    public SortCondition convertToEntityAttribute(String description) {
        return Arrays.stream(SortCondition.values())
                .filter(sortCondition -> sortCondition.getDescription().equals(description))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("찾으려는 정렬 조건이 존재하지 않습니다."));
    }
}