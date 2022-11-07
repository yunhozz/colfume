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

    @Column(length = 100)
    private String content;

    private double score;

    private boolean isDeleted;

    @Builder
    private Evaluation(Member writer, Perfume perfume, String content, double score) {
        this.writer = writer;
        this.perfume = perfume;
        this.content = content;
        this.score = score;
    }

    public void update(String content, double score) {
        this.content = content;
        this.score = score;
    }

    public void delete() {
        if (!isDeleted) {
            isDeleted = true;
            content = "관리자 또는 작성자에 의해 삭제되었습니다.";
            perfume.subtractEvaluationCount();
        } else {
            throw new IllegalStateException("이미 삭제된 평가입니다.");
        }
    }
}