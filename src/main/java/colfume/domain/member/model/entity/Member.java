package colfume.domain.member.model.entity;

import colfume.common.enums.Role;
import colfume.domain.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, unique = true)
    private String email;

    private String password;

    @Column(length = 10)
    private String name;

    private String imageUrl;

    private boolean isEmailVerified; // 이메일 인증 여부

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    private Member(String email, String password, String name, String imageUrl, Role role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.imageUrl = imageUrl;
        this.role = role;
    }

    public Member update(String email, String name, String imageUrl) {
        this.email = email;
        this.name = name;
        this.imageUrl = imageUrl;

        return this;
    }

    public boolean isEmailEqualsWith(String email) {
        return email.equals(this.email);
    }

    public boolean isPasswordNotEqualsWith(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return !encoder.matches(password, this.password);
    }

    public boolean isEmailNotVerified() {
        return !isEmailVerified;
    }

    public void updateInfo(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public void updatePassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        this.password = encoder.encode(password);
    }

    public void emailVerify() {
        isEmailVerified = true;
        role = Role.USER;
    }

    // initDb 테스트용
    public void updateRole(Role role) {
        this.role = role;
    }
}