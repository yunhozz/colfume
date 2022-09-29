package colfume.util;

import colfume.api.dto.Response;
import colfume.dto.ErrorResponseDto;
import colfume.enums.ErrorCode;
import colfume.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

import static colfume.dto.ErrorResponseDto.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Response handleException(Exception e) {
        log.error("handleException", e);
        ErrorResponseDto error = new ErrorResponseDto(ErrorCode.INTER_SERVER_ERROR);

        return Response.failure(-1000, error, HttpStatus.valueOf(error.getStatus()));
    }

    @ExceptionHandler(RuntimeException.class)
    public Response handleRuntimeException(RuntimeException e) {
        log.error("handleRuntimeException", e);
        ErrorResponseDto error = new ErrorResponseDto(ErrorCode.INTER_SERVER_ERROR);

        return Response.failure(-1000, error, HttpStatus.valueOf(error.getStatus()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Response handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("handleMethodArgumentNotValidException", e);
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        List<NotValidResponseDto> notValidResponseDtoList = new ArrayList<>();

        fieldErrors.forEach(fieldError ->  {
            NotValidResponseDto notValidResponseDto = NotValidResponseDto.builder()
                    .field(fieldError.getField())
                    .code(fieldError.getCode())
                    .rejectValue(fieldError.getRejectedValue())
                    .defaultMessage(fieldError.getDefaultMessage())
                    .build();
            notValidResponseDtoList.add(notValidResponseDto);
        });

        ErrorResponseDto error = new ErrorResponseDto(ErrorCode.NOT_VALID, notValidResponseDtoList);
        return Response.failure(-1000, error, HttpStatus.valueOf(error.getStatus()));
    }

    @ExceptionHandler(MemberNotFoundException.class)
    public Response handleMemberNotFoundException(MemberNotFoundException e) {
        log.error("handleMemberNotFoundException", e);
        ErrorResponseDto error = new ErrorResponseDto(e.getErrorCode());

        return Response.failure(-1000, error, HttpStatus.valueOf(error.getStatus()));
    }

    @ExceptionHandler(EmailNotFoundException.class)
    public Response handleEmailNotFoundException(EmailNotFoundException e) {
        log.error("handleEmailNotFoundException", e);
        ErrorResponseDto error = new ErrorResponseDto(e.getErrorCode());

        return Response.failure(-1000, error, HttpStatus.valueOf(error.getStatus()));
    }

    @ExceptionHandler(ConfirmationTokenNotFoundException.class)
    public Response handleConfirmationTokenNotFoundException(ConfirmationTokenNotFoundException e) {
        log.error("handleConfirmationTokenNotFoundException", e);
        ErrorResponseDto error = new ErrorResponseDto(e.getErrorCode());

        return Response.failure(-1000, error, HttpStatus.valueOf(error.getStatus()));
    }

    @ExceptionHandler(EmailNotVerifiedException.class)
    public Response handleEmailNotVerifiedException(EmailNotVerifiedException e) {
        log.error("handleEmailNotVerifiedException", e);
        ErrorResponseDto error = new ErrorResponseDto(e.getErrorCode());

        return Response.failure(-1000, error, HttpStatus.valueOf(error.getStatus()));
    }

    @ExceptionHandler(EmailDuplicateException.class)
    public Response handleEmailDuplicateException(EmailDuplicateException e) {
        log.error("handleEmailDuplicateException", e);
        ErrorResponseDto error = new ErrorResponseDto(e.getErrorCode());

        return Response.failure(-1000, error, HttpStatus.valueOf(error.getStatus()));
    }

    @ExceptionHandler(PasswordMismatchException.class)
    public Response handlePasswordMismatchException(PasswordMismatchException e) {
        log.error("handlePasswordMismatchException", e);
        ErrorResponseDto error = new ErrorResponseDto(e.getErrorCode());

        return Response.failure(-1000, error, HttpStatus.valueOf(error.getStatus()));
    }

    @ExceptionHandler(PasswordSameException.class)
    public Response handlePasswordSameException(PasswordSameException e) {
        log.error("handlePasswordSameException", e);
        ErrorResponseDto error = new ErrorResponseDto(e.getErrorCode());

        return Response.failure(-1000, error, HttpStatus.valueOf(error.getStatus()));
    }

    @ExceptionHandler(PerfumeNotFoundException.class)
    public Response handlePerfumeNotFoundException(PerfumeNotFoundException e) {
        log.error("handlePerfumeNotFoundException", e);
        ErrorResponseDto error = new ErrorResponseDto(e.getErrorCode());

        return Response.failure(-1000, error, HttpStatus.valueOf(error.getStatus()));
    }

    @ExceptionHandler(NotificationNotFoundException.class)
    public Response handleNotificationNotFoundException(NotificationNotFoundException e) {
        log.error("handleNotificationNotFoundException", e);
        ErrorResponseDto error = new ErrorResponseDto(e.getErrorCode());

        return Response.failure(-1000, error, HttpStatus.valueOf(error.getStatus()));
    }

    @ExceptionHandler(NotificationSendFailException.class)
    public Response handleNotificationSendFailException(NotificationSendFailException e) {
        log.error("handleNotificationSendFailException", e);
        ErrorResponseDto error = new ErrorResponseDto(e.getErrorCode());

        return Response.failure(-1000, error, HttpStatus.valueOf(error.getStatus()));
    }

    @ExceptionHandler(ChatroomNotFoundException.class)
    public Response handleChatroomNotFoundException(ChatroomNotFoundException e) {
        log.error("handleChatroomNotFoundException", e);
        ErrorResponseDto error = new ErrorResponseDto(e.getErrorCode());

        return Response.failure(-1000, error, HttpStatus.valueOf(error.getStatus()));
    }

    @ExceptionHandler(ChatroomPermissionException.class)
    public Response handleChatroomPermissionException(ChatroomPermissionException e) {
        log.error("handleChatroomPermissionException", e);
        ErrorResponseDto error = new ErrorResponseDto(e.getErrorCode());

        return Response.failure(-1000, error, HttpStatus.valueOf(error.getStatus()));
    }
}