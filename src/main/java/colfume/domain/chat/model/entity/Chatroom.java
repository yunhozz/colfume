package colfume.domain.chat.model.entity;

import colfume.domain.BaseEntity;
import colfume.domain.member.model.entity.Member;
import lombok.AccessLevel;
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
public class Chatroom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(length = 50)
    private String title;

    public Chatroom(Member member, String title) {
        this.member = member;
        this.title = title;
    }

    public boolean isMemberNotEqualsWith(Long userId) {
        return member.isUserIdNotEqualsWith(userId);
    }

    public void updateTitle(String title) {
        this.title = title;
    }
}