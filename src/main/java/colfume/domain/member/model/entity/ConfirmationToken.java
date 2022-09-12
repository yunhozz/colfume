package colfume.domain.member.model.entity;

import colfume.domain.BaseTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ConfirmationToken extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private LocalDateTime expirationDate; // 만료 시간

    private boolean isExpired; // 만료 여부

    public ConfirmationToken(Long userId, LocalDateTime expirationDate) {
        this.userId = userId;
        this.expirationDate = expirationDate;
    }

    public void useToken() {
        isExpired = true;
    }
}