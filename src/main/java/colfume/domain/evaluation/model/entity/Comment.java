package colfume.domain.evaluation.model.entity;

import colfume.domain.BaseTime;
import colfume.domain.member.model.entity.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

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

    @ManyToOne(fetch = FetchType.LAZY)
    private Comment parent; // self join

    @OneToMany(mappedBy = "parent", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> children = new ArrayList<>(); // 대댓글

    @Column(length = 50)
    private String content;

    private Comment(Member writer, Evaluation evaluation, String content) {
        this.writer = writer;
        this.evaluation = evaluation;
        this.content = content;
    }

    public static Comment createParent(Member writer, Evaluation evaluation, String content) {
        return new Comment(writer, evaluation, content);
    }

    public static Comment createChild(Comment parent, Member writer, Evaluation evaluation, String content) {
        Comment comment = createParent(writer, evaluation, content);
        comment.setParent(parent);
        comment.getChildren().add(parent);

        return comment;
    }

    public boolean hasParent() {
        return parent != null;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    private void setParent(Comment parent) {
        this.parent = parent;
    }
}