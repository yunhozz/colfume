package colfume.domain.notification.model;

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
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member sender;

    @ManyToOne(fetch = FetchType.LAZY)
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
