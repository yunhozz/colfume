package colfume.domain.notification.model.entity;

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
public class Notification extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private Member sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private Member receiver;

    private String message;

    private String redirectUrl;

    private boolean isChecked;

    @Builder
    private Notification(Member sender, Member receiver, String message, String redirectUrl) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.redirectUrl = redirectUrl;
    }

    public void check() {
        isChecked = true;
    }
}
