package colfume.domain.member.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Authority {

    @Id
    private String role; // ROLE_ADMIN, ROLE_USER

    public Authority(String role) {
        this.role = role;
    }
}
