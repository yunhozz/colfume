package colfume.common.converter.enums;

import colfume.common.enums.SortCondition;

import java.util.Arrays;

public class SortConditionConverter implements EnumConverter<SortCondition, String> {

    @Override
    public SortCondition convertToEnum(String description) {
        return Arrays.stream(SortCondition.values())
                .filter(sortCondition -> sortCondition.getDescription().equals(description))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("찾으려는 정렬 조건이 존재하지 않습니다."));
    }

    @Override
    public String convertToValue(SortCondition sortCondition) {
        return sortCondition.getDescription();
    }
}