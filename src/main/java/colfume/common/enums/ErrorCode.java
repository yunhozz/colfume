package colfume.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // global
    NOT_VALID(400, "G-001", "입력값이 올바르지 않습니다."),
    ALREADY_DELETED(400, "G-002", "이미 삭제된 데이터입니다."),
    UNAUTHORIZED(401, "G-011", "사용자에 대한 권한이 없습니다."),
    NOT_AUTHENTICATED(401, "G-012", "수정 또는 삭제 권한이 없습니다."),
    FORBIDDEN(403, "G-031", "접근이 거부된 사용자입니다."),
    COOKIE_NOT_FOUND(404, "G-041", "찾으려는 쿠키가 존재하지 않습니다."),
    INTER_SERVER_ERROR(500, "G-500", "내부 서버 에러가 발생했습니다."),

    // member
    EMAIL_NOT_VERIFIED(400, "M-001", "이메일 인증을 완료해주세요."),
    EMAIL_DUPLICATE(400, "M-002", "중복되는 이메일이 존재합니다."),
    PASSWORD_MISMATCH(400, "M-003", "비밀번호가 일치하지 않습니다."),
    PASSWORD_SAME(400, "M-004", "기존 비밀번호와 일치합니다."),
    NAME_NOT_INSERTED(400, "M-005", "변경하실 이름을 입력해주세요."),
    REFRESH_TOKEN_NOT_CORRESPOND(400, "M-006", "기존 재발급 토큰과 일치하지 않습니다."),
    MEMBER_NOT_FOUND(404, "M-041", "찾으려는 유저가 존재하지 않습니다."),
    EMAIL_NOT_FOUND(404, "M-042", "이메일이 존재하지 않습니다."),
    CONFIRMATION_TOKEN_NOT_FOUND(404, "M-043", "이메일 인증 토큰을 찾을 수 없습니다."),
    REFRESH_TOKEN_NOT_FOUND(404, "M-044", "재발급 토큰이 존재하지 않습니다."),

    // perfume
    PERFUME_NOT_FOUND(404, "P-041", "찾으려는 향수가 존재하지 않습니다."),

    // notification
    NOTIFICATION_SEND_FAIL(400, "N-001", "알림 전송에 실패했습니다."),
    NOTIFICATION_NOT_FOUND(404, "N-041", "찾으려는 알림이 존재하지 않습니다."),

    // chat
    CHATROOM_TITLE_NOT_INSERTED(400, "C-001", "채팅방 이름을 입력해주세요."),
    CHATROOM_NOT_PERMISSION(401, "C-011", "채팅방 수정 권한이 없습니다."),
    CHATROOM_NOT_FOUND(404, "C-041", "찾으려는 채팅방이 존재하지 않습니다."),

    // bookmark
    BOOKMARK_ALREADY_CREATED(400, "B-001", "이미 생성된 북마크입니다."),
    BOOKMARK_NOT_FOUND(404, "B-041", "찾으려는 북마크가 존재하지 않습니다."),

    // evaluation
    EVALUATION_ALREADY_EXIST(400, "E-001", "이미 작성한 평가가 존재합니다."),
    EVALUATION_ALREADY_DELETED(400, "E-002", "찾으려는 평가가 이미 삭제된 상태입니다."),
    EVALUATION_NOT_FOUND(404, "E-041", "찾으려는 평가가 존재하지 않습니다."),
    COMMENT_NOT_FOUND(404, "E-042", "찾으려는 댓글이 존재하지 않습니다."),

    ;

    private final int status;
    private final String code;
    private final String message;
}
