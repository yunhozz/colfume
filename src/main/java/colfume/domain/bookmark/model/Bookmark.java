package colfume.domain.bookmark.model;

import colfume.domain.BaseEntity;
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
public class Bookmark extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Perfume perfume;

    private String redirectUrl;

    public Bookmark(Member member, Perfume perfume, String redirectUrl) {
        this.member = member;
        this.perfume = perfume;
        this.redirectUrl = redirectUrl;
    }

    public void subtractPerfumeLikes() {
        perfume.subtractLikes();
    }
}