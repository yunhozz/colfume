package colfume.domain.perfume.model.entity;

import colfume.domain.BaseTime;
import colfume.enums.ColorType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Color extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "perfume_id")
    private Perfume perfume;

    @Enumerated(EnumType.STRING)
    private ColorType colorType;

    public Color(ColorType colorType) {
        this.colorType = colorType;
    }

    public void updatePerfume(Perfume perfume) {
        this.perfume = perfume;
    }
}