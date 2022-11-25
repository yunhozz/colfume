package colfume.common;

import colfume.common.dto.ErrorResponseDto;
import colfume.common.dto.Response;
import colfume.common.enums.ErrorCode;
import colfume.domain.bookmark.service.exception.BookmarkAlreadyCreatedException;
import colfume.domain.bookmark.service.exception.BookmarkNotFoundException;
import colfume.domain.chat.service.exception.ChatroomNotFoundException;
import colfume.domain.chat.service.exception.ChatroomPermissionException;
import colfume.domain.evaluation.service.exception.CommentNotFoundException;
import colfume.domain.evaluation.service.exception.CrudNotAuthenticationException;
import colfume.domain.evaluation.service.exception.EvaluationAlreadyDeletedException;
import colfume.domain.evaluation.service.exception.EvaluationAlreadyExistException;
import colfume.domain.evaluation.service.exception.EvaluationNotFoundException;
import colfume.domain.member.service.exception.ConfirmationTokenNotFoundException;
import colfume.domain.member.service.exception.EmailDuplicateException;
import colfume.domain.member.service.exception.EmailNotFoundException;
import colfume.domain.member.service.exception.EmailNotVerifiedException;
import colfume.domain.member.service.exception.MemberNotFoundException;
import colfume.domain.member.service.exception.PasswordMismatchException;
import colfume.domain.member.service.exception.PasswordSameException;
import colfume.domain.member.service.exception.RefreshTokenNotCorrespondException;
import colfume.domain.member.service.exception.RefreshTokenNotFoundException;
import colfume.domain.notification.service.exception.NotificationNotFoundException;
import colfume.domain.notification.service.exception.NotificationSendFailException;
import colfume.domain.perfume.service.PerfumeNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

import static colfume.common.dto.ErrorResponseDto.NotValidResponseDto;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // TODO: 2022-11-25 에러 코드 수정

    @ExceptionHandler(RuntimeException.class)
    public Response handleRuntimeException(RuntimeException e) {
        // Security 와 관련된 Exception 발생 이전에 RuntimeException 이 먼저 발생하므로 제외
        if (e instanceof AccessDeniedException) {
            log.error("handleAccessDeniedException", e);
            ErrorResponseDto errorResponseDto = new ErrorResponseDto(ErrorCode.FORBIDDEN);
            return Response.failure(-1000, errorResponseDto, HttpStatus.valueOf(errorResponseDto.getStatus()));
        }

        if (e instanceof AuthenticationException) {
            log.error("handleAuthenticationException", e);
            ErrorResponseDto errorResponseDto = new ErrorResponseDto(ErrorCode.UNAUTHORIZED);
            return Response.failure(-1000, errorResponseDto, HttpStatus.valueOf(errorResponseDto.getStatus()));
        }

        log.error("handleRuntimeException", e);
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(ErrorCode.INTER_SERVER_ERROR);
        return Response.failure(-1000, errorResponseDto, HttpStatus.valueOf(errorResponseDto.getStatus()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public Response handleIllegalStateException(IllegalStateException e) {
        log.error("handleIllegalStateException", e);
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(ErrorCode.NOT_VALID);
        return Response.failure(-1000, errorResponseDto, HttpStatus.valueOf(errorResponseDto.getStatus()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Response handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("handleMethodArgumentNotValidException", e);
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();

        List<NotValidResponseDto> notValidResponseDtoList = new ArrayList<>() {{
            fieldErrors.forEach(fieldError -> {
                NotValidResponseDto notValidResponseDto = NotValidResponseDto.builder()
                        .field(fieldError.getField())
                        .code(fieldError.getCode())
                        .rejectValue(fieldError.getRejectedValue())
                        .defaultMessage(fieldError.getDefaultMessage())
                        .build();

                add(notValidResponseDto);
            });
        }};

        ErrorResponseDto errorResponseDto = new ErrorResponseDto(ErrorCode.NOT_VALID, notValidResponseDtoList);
        return Response.failure(-1000, errorResponseDto, HttpStatus.valueOf(errorResponseDto.getStatus()));
    }

    @ExceptionHandler(CrudNotAuthenticationException.class)
    public Response handleCrudNotAuthenticationException(CrudNotAuthenticationException e) {
        log.error("handleCrudNotAuthenticationException", e);
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(e.getErrorCode());
        return Response.failure(-1000, errorResponseDto, HttpStatus.valueOf(errorResponseDto.getStatus()));
    }

    @ExceptionHandler(MemberNotFoundException.class)
    public Response handleMemberNotFoundException(MemberNotFoundException e) {
        log.error("handleMemberNotFoundException", e);
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(e.getErrorCode());
        return Response.failure(-1000, errorResponseDto, HttpStatus.valueOf(errorResponseDto.getStatus()));
    }

    @ExceptionHandler(EmailNotFoundException.class)
    public Response handleEmailNotFoundException(EmailNotFoundException e) {
        log.error("handleEmailNotFoundException", e);
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(e.getErrorCode());
        return Response.failure(-1000, errorResponseDto, HttpStatus.valueOf(errorResponseDto.getStatus()));
    }

    @ExceptionHandler(ConfirmationTokenNotFoundException.class)
    public Response handleConfirmationTokenNotFoundException(ConfirmationTokenNotFoundException e) {
        log.error("handleConfirmationTokenNotFoundException", e);
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(e.getErrorCode());
        return Response.failure(-1000, errorResponseDto, HttpStatus.valueOf(errorResponseDto.getStatus()));
    }

    @ExceptionHandler(EmailNotVerifiedException.class)
    public Response handleEmailNotVerifiedException(EmailNotVerifiedException e) {
        log.error("handleEmailNotVerifiedException", e);
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(e.getErrorCode());
        return Response.failure(-1000, errorResponseDto, HttpStatus.valueOf(errorResponseDto.getStatus()));
    }

    @ExceptionHandler(EmailDuplicateException.class)
    public Response handleEmailDuplicateException(EmailDuplicateException e) {
        log.error("handleEmailDuplicateException", e);
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(e.getErrorCode());
        return Response.failure(-1000, errorResponseDto, HttpStatus.valueOf(errorResponseDto.getStatus()));
    }

    @ExceptionHandler(PasswordMismatchException.class)
    public Response handlePasswordMismatchException(PasswordMismatchException e) {
        log.error("handlePasswordMismatchException", e);
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(e.getErrorCode());
        return Response.failure(-1000, errorResponseDto, HttpStatus.valueOf(errorResponseDto.getStatus()));
    }

    @ExceptionHandler(PasswordSameException.class)
    public Response handlePasswordSameException(PasswordSameException e) {
        log.error("handlePasswordSameException", e);
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(e.getErrorCode());
        return Response.failure(-1000, errorResponseDto, HttpStatus.valueOf(errorResponseDto.getStatus()));
    }

    @ExceptionHandler(RefreshTokenNotFoundException.class)
    public Response handleRefreshTokenNotFoundException(RefreshTokenNotFoundException e) {
        log.error("handleRefreshTokenNotFoundException", e);
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(e.getErrorCode());
        return Response.failure(-1000, errorResponseDto, HttpStatus.valueOf(errorResponseDto.getStatus()));
    }

    @ExceptionHandler(RefreshTokenNotCorrespondException.class)
    public Response handleRefreshTokenNotCorrespondException(RefreshTokenNotCorrespondException e) {
        log.error("handleRefreshTokenNotCorrespondException", e);
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(e.getErrorCode());
        return Response.failure(-1000, errorResponseDto, HttpStatus.valueOf(errorResponseDto.getStatus()));
    }

    @ExceptionHandler(PerfumeNotFoundException.class)
    public Response handlePerfumeNotFoundException(PerfumeNotFoundException e) {
        log.error("handlePerfumeNotFoundException", e);
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(e.getErrorCode());
        return Response.failure(-1000, errorResponseDto, HttpStatus.valueOf(errorResponseDto.getStatus()));
    }

    @ExceptionHandler(NotificationNotFoundException.class)
    public Response handleNotificationNotFoundException(NotificationNotFoundException e) {
        log.error("handleNotificationNotFoundException", e);
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(e.getErrorCode());
        return Response.failure(-1000, errorResponseDto, HttpStatus.valueOf(errorResponseDto.getStatus()));
    }

    @ExceptionHandler(NotificationSendFailException.class)
    public Response handleNotificationSendFailException(NotificationSendFailException e) {
        log.error("handleNotificationSendFailException", e);
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(e.getErrorCode());
        return Response.failure(-1000, errorResponseDto, HttpStatus.valueOf(errorResponseDto.getStatus()));
    }

    @ExceptionHandler(ChatroomNotFoundException.class)
    public Response handleChatroomNotFoundException(ChatroomNotFoundException e) {
        log.error("handleChatroomNotFoundException", e);
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(e.getErrorCode());
        return Response.failure(-1000, errorResponseDto, HttpStatus.valueOf(errorResponseDto.getStatus()));
    }

    @ExceptionHandler(ChatroomPermissionException.class)
    public Response handleChatroomPermissionException(ChatroomPermissionException e) {
        log.error("handleChatroomPermissionException", e);
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(e.getErrorCode());
        return Response.failure(-1000, errorResponseDto, HttpStatus.valueOf(errorResponseDto.getStatus()));
    }

    @ExceptionHandler(BookmarkNotFoundException.class)
    public Response handleBookmarkNotFoundException(BookmarkNotFoundException e) {
        log.error("handleBookmarkNotFoundException", e);
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(e.getErrorCode());
        return Response.failure(-1000, errorResponseDto, HttpStatus.valueOf(errorResponseDto.getStatus()));
    }

    @ExceptionHandler(BookmarkAlreadyCreatedException.class)
    public Response handleBookmarkAlreadyCreatedException(BookmarkAlreadyCreatedException e) {
        log.error("handleBookmarkAlreadyCreatedException", e);
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(e.getErrorCode());
        return Response.failure(-1000, errorResponseDto, HttpStatus.valueOf(errorResponseDto.getStatus()));
    }

    @ExceptionHandler(EvaluationNotFoundException.class)
    public Response handleEvaluationNotFoundException(EvaluationNotFoundException e) {
        log.error("handleEvaluationNotFoundException", e);
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(e.getErrorCode());
        return Response.failure(-1000, errorResponseDto, HttpStatus.valueOf(errorResponseDto.getStatus()));
    }

    @ExceptionHandler(CommentNotFoundException.class)
    public Response handleCommentNotFoundException(CommentNotFoundException e) {
        log.error("handleCommentNotFoundException", e);
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(e.getErrorCode());
        return Response.failure(-1000, errorResponseDto, HttpStatus.valueOf(errorResponseDto.getStatus()));
    }

    @ExceptionHandler(EvaluationAlreadyDeletedException.class)
    public Response handleEvaluationAlreadyDeletedException(EvaluationAlreadyDeletedException e) {
        log.error("handleEvaluationAlreadyDeletedException", e);
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(e.getErrorCode());
        return Response.failure(-1000, errorResponseDto, HttpStatus.valueOf(errorResponseDto.getStatus()));
    }

    @ExceptionHandler(EvaluationAlreadyExistException.class)
    public Response handleEvaluationAlreadyExistException(EvaluationAlreadyExistException e) {
        log.error("handleEvaluationAlreadyExistException", e);
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(e.getErrorCode());
        return Response.failure(-1000, errorResponseDto, HttpStatus.valueOf(errorResponseDto.getStatus()));
    }
}