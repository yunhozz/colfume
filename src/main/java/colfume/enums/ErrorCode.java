package colfume.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // global
    NOT_FOUND(404, "COMMON-ERR-404", "PAGE NOT FOUND"),
    INTER_SERVER_ERROR(500, "COMMON-ERR-500", "INTER SERVER ERROR"),

    // member
    MEMBER_NOT_FOUND(400, "MEMBER-ERR-400", "찾으려는 유저가 존재하지 않습니다."),
    EMAIL_NOT_FOUND(400, "MEMBER-ERR-400", "이메일이 존재하지 않습니다."),
    EMAIL_DUPLICATE(400, "MEMBER-ERR-400", "중복되는 이메일이 존재합니다."),
    PASSWORD_MISMATCH(400, "MEMBER-ERR-400", "비밀번호가 일치하지 않습니다."),

    // perfume
    PERFUME_NOT_FOUND(400, "PERFUME-ERR-400", "찾으려는 향수가 존재하지 않습니다."),

    // notification
    NOTIFICATION_NOT_FOUND(400, "NOTIFICATION-ERR-400", "찾으려는 알림이 존재하지 않습니다.");

    private final int status;
    private final String code;
    private final String message;
}
