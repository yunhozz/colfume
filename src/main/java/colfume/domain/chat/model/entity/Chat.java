package colfume.domain.chat.model.entity;

import colfume.domain.BaseEntity;
import colfume.domain.member.model.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
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
public class Chat extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member sender;

    @ManyToOne(fetch = FetchType.LAZY)
    private Chatroom chatroom;

    private String message;

    @Builder
    private Chat(Member sender, Chatroom chatroom, String message) {
        this.sender = sender;
        this.chatroom = chatroom;
        this.message = message;
    }
}
