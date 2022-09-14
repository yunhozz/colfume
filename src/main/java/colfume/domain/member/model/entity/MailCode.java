package colfume.domain.member.model.entity;

import colfume.domain.BaseTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MailCode extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String email;

    @Column(length = 8)
    private String code;

    private boolean isVerified;

    public MailCode(String email, String code, boolean isVerified) {
        this.email = email;
        this.code = code;
        this.isVerified = isVerified;
    }

    public void verified() {
        isVerified = true;
    }
}