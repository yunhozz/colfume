package colfume.domain.chat.model.entity;

import colfume.domain.BaseTime;
import colfume.domain.member.model.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chat extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private Member sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatroom_id")
    private Chatroom chatroom;

    private String message;

    @Builder
    private Chat(Member sender, Chatroom chatroom, String message) {
        this.sender = sender;
        this.chatroom = chatroom;
        this.message = message;
    }
}
