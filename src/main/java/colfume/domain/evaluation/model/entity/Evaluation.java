package colfume.domain.evaluation.model.entity;

import colfume.domain.BaseTime;
import colfume.domain.member.model.entity.Member;
import colfume.domain.perfume.model.entity.Perfume;
import colfume.common.enums.ErrorCode;
import colfume.domain.evaluation.service.exception.AlreadyDeletedException;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Evaluation extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    private Member writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "perfume_id")
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
            throw new AlreadyDeletedException(ErrorCode.ALREADY_DELETED);
        }
    }
}