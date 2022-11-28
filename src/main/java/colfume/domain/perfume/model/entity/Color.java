package colfume.domain.perfume.model.entity;

import colfume.common.enums.ColorType;
import colfume.domain.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Color extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Perfume perfume;

    @Convert(converter = ColorTypeConverter.class)
    private ColorType colorType;

    public Color(ColorType colorType) {
        this.colorType = colorType;
    }

    public void updatePerfume(Perfume perfume) {
        this.perfume = perfume;
    }
}