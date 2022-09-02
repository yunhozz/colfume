package colfume.domain.perfume.model.entity;

import colfume.domain.BaseTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Hashtag extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "perfume_id")
    private Perfume perfume;

    @Column(length = 10)
    private String tag;

    public Hashtag(String tag) {
        this.tag = tag;
    }

    public void updatePerfume(Perfume perfume) {
        this.perfume = perfume;
    }
}
