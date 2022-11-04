package colfume.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // TODO: http status 값 수정

    // global
    NOT_FOUND(404, "PAGE NOT FOUND"),
    INTER_SERVER_ERROR(500, "INTER SERVER ERROR"),
    NOT_VALID(400, "입력값이 올바르지 않습니다."),
    ALREADY_DELETED(400, "이미 삭제된 데이터입니다."),
    NOT_AUTHENTICATED(400, "수정 또는 삭제 권한이 없습니다."),
    COOKIE_NOT_FOUND(400, "찾으려는 쿠키가 존재하지 않습니다."),

    // member
    MEMBER_NOT_FOUND(400, "찾으려는 유저가 존재하지 않습니다."),
    EMAIL_NOT_FOUND(400, "이메일이 존재하지 않습니다."),
    CONFIRMATION_TOKEN_NOT_FOUND(400, "이메일 인증 토큰을 찾을 수 없습니다."),
    EMAIL_NOT_VERIFIED(400, "이메일 인증을 완료해주세요."),
    EMAIL_DUPLICATE(400, "중복되는 이메일이 존재합니다."),
    PASSWORD_MISMATCH(400, "비밀번호가 일치하지 않습니다."),
    PASSWORD_SAME(400, "기존 비밀번호와 일치합니다."),
    NAME_NOT_INSERTED(400, "변경하실 이름을 입력해주세요."),
    REFRESH_TOKEN_NOT_FOUND(400, "재발급 토큰이 존재하지 않습니다."),
    REFRESH_TOKEN_NOT_CORRESPOND(400, "기존 재발급 토큰과 일치하지 않습니다."),

    // perfume
    PERFUME_NOT_FOUND(400, "찾으려는 향수가 존재하지 않습니다."),

    // notification
    NOTIFICATION_NOT_FOUND(400, "찾으려는 알림이 존재하지 않습니다."),
    NOTIFICATION_SEND_FAIL(400, "알림 전송에 실패했습니다."),

    // chat
    CHATROOM_NOT_FOUND(400, "찾으려는 채팅방이 존재하지 않습니다."),
    CHATROOM_NOT_PERMISSION(400, "채팅방 수정 권한이 없습니다."),
    CHATROOM_TITLE_NOT_INSERTED(400, "채팅방 이름을 입력해주세요.");

    private final int status;
    private final String message;
}
