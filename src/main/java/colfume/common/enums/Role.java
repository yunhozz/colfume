package colfume.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Getter
@RequiredArgsConstructor
public enum Role implements GrantedAuthority {

    ADMIN("ROLE_ADMIN", "운영자"),
    USER("ROLE_USER", "일반 사용자"),
    GUEST("ROLE_GUEST", "게스트");

    private final String key;
    private final String value;

    @Override
    public String getAuthority() {
        return key;
    }
}