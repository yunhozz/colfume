package colfume.domain.chat.model.entity;

import colfume.domain.BaseTime;
import colfume.domain.member.model.entity.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chatroom extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(length = 50)
    private String title;

    public Chatroom(Member member, String title) {
        this.member = member;
        this.title = title;
    }

    public void updateTitle(String title) {
        this.title = title;
    }
}
