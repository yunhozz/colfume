package colfume.common.converter;

import colfume.common.enums.SearchCondition;

import java.util.Arrays;

public class SearchConditionConverter implements ModelConverter<String, SearchCondition> {

    @Override
    public SearchCondition convertToObject(String description) {
        return Arrays.stream(SearchCondition.values())
                .filter(searchCondition -> searchCondition.getDescription().equals(description))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("찾으려는 검색 조건이 존재하지 않습니다."));
    }

    @Override
    public String convertFromObject(SearchCondition searchCondition) {
        return searchCondition.getDescription();
    }
}