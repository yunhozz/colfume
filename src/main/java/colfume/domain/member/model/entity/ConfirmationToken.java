package colfume.domain.member.model.entity;

import colfume.domain.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ConfirmationToken extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(insertable = false, updatable = false)
    private Member user;

    @Column(name = "user_id", length = 64, unique = true)
    private Long userId;

    private LocalDateTime expirationDate; // 만료 시간

    private boolean isExpired; // 만료 여부

    public ConfirmationToken(Long userId, LocalDateTime expirationDate) {
        this.userId = userId;
        this.expirationDate = expirationDate;
    }

    public void useTokenAndVerify() {
        isExpired = true;
        user.emailVerify();
    }
}