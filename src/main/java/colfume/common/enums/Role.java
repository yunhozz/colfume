package colfume.common.enums;

import lombok.Getter;

@Getter
public enum Role {

    ADMIN("ROLE_ADMIN", "운영자"),
    USER("ROLE_USER", "일반 사용자"),
    GUEST("ROLE_GUEST", "게스트");

    private final String key;
    private final String value;

    Role(String key, String value) {
        this.key = key;
        this.value = value;
    }
}