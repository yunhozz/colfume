package colfume.domain.bookmark.model.entity;

import colfume.domain.BaseTime;
import colfume.domain.member.model.entity.Member;
import colfume.domain.perfume.model.entity.Perfume;
import colfume.enums.ErrorCode;
import colfume.exception.AlreadyDeletedException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Bookmark extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "perfume_id")
    private Perfume perfume;

    private String redirectUrl;

    private boolean isDeleted;

    public Bookmark(Member member, Perfume perfume, String redirectUrl) {
        this.member = member;
        this.perfume = perfume;
        this.redirectUrl = redirectUrl;
    }

    public void delete() {
        if (!isDeleted) {
            isDeleted = true;
        } else {
            throw new AlreadyDeletedException(ErrorCode.ALREADY_DELETED);
        }
    }
}