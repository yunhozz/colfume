package colfume.domain.evaluation.model.entity;

import colfume.domain.BaseTime;
import colfume.domain.member.model.entity.Member;
import colfume.domain.perfume.model.entity.Perfume;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Evaluation extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member writer;

    @ManyToOne(fetch = FetchType.LAZY)
    private Perfume perfume;

    private String content;

    @Column(columnDefinition = "tinyint")
    private int score;

    private boolean isDeleted;

    @Builder
    private Evaluation(Member writer, Perfume perfume, String content, int score) {
        this.writer = writer;
        this.perfume = perfume;
        this.content = content;
        this.score = score;
    }

    public void update(String content, int score) {
        this.content = content;
        this.score = score;
    }

    public void delete() {
        if (!isDeleted) {
            isDeleted = true;
        } else {
            throw new IllegalStateException("이미 삭제된 평가입니다.");
        }
    }
}