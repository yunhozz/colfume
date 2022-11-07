package colfume.domain.bookmark.model.entity;

import colfume.domain.BaseTime;
import colfume.domain.member.model.entity.Member;
import colfume.domain.perfume.model.entity.Perfume;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Bookmark extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Perfume perfume;

    private String redirectUrl;

    private boolean isDeleted;

    public Bookmark(Member member, Perfume perfume, String redirectUrl) {
        this.member = member;
        this.perfume = perfume;
        this.redirectUrl = redirectUrl;
    }

    public void create() {
        if (isDeleted) {
            isDeleted = false;
            perfume.addLikes();

        } else {
            throw new IllegalStateException("이미 생성한 북마크입니다.");
        }
    }

    public void delete() {
        if (!isDeleted) {
            isDeleted = true;
            perfume.subtractLikes();

        } else {
            throw new IllegalStateException("이미 삭제된 북마크입니다.");
        }
    }
}