package colfume.domain.evaluation.model.entity;

import colfume.domain.BaseTime;
import colfume.domain.member.model.entity.Member;
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
public class Comment extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member writer;

    @ManyToOne(fetch = FetchType.LAZY)
    private Evaluation evaluation;

    @Column(length = 50)
    private String content;

    @Builder
    private Comment(Member writer, Evaluation evaluation, String content) {
        this.writer = writer;
        this.evaluation = evaluation;
        this.content = content;
    }

    public void updateContent(String content) {
        this.content = content;
    }
}