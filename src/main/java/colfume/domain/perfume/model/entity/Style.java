package colfume.domain.perfume.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Style {

    private String styleValue;

    public Style(String styleValue) {
        this.styleValue = styleValue;
    }
}