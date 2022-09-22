package colfume.util;

import colfume.dto.ErrorResponseDto;
import colfume.enums.ErrorCode;
import colfume.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

import static colfume.dto.ErrorResponseDto.*;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleException(Exception e) {
        log.error("handleException", e);
        ErrorResponseDto error = new ErrorResponseDto(ErrorCode.INTER_SERVER_ERROR);

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponseDto> handleRuntimeException(RuntimeException e) {
        log.error("handleRuntimeException", e);
        ErrorResponseDto error = new ErrorResponseDto(ErrorCode.INTER_SERVER_ERROR);

        return new ResponseEntity<>(error, HttpStatus.REQUEST_TIMEOUT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
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

        return new ResponseEntity<>(new ErrorResponseDto(ErrorCode.NOT_VALID, notValidResponseDtoList), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleMemberNotFoundException(MemberNotFoundException e) {
        log.error("handleMemberNotFoundException", e);
        ErrorResponseDto error = new ErrorResponseDto(e.getErrorCode());

        return new ResponseEntity<>(error, HttpStatus.valueOf(e.getErrorCode().getStatus()));
    }

    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleEmailNotFoundException(EmailNotFoundException e) {
        log.error("handleEmailNotFoundException", e);
        ErrorResponseDto error = new ErrorResponseDto(e.getErrorCode());

        return new ResponseEntity<>(error, HttpStatus.valueOf(e.getErrorCode().getStatus()));
    }

    @ExceptionHandler(ConfirmationTokenNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleConfirmationTokenNotFoundException(ConfirmationTokenNotFoundException e) {
        log.error("handleConfirmationTokenNotFoundException", e);
        ErrorResponseDto error = new ErrorResponseDto(e.getErrorCode());

        return new ResponseEntity<>(error, HttpStatus.valueOf(e.getErrorCode().getStatus()));
    }

    @ExceptionHandler(EmailNotVerifiedException.class)
    public ResponseEntity<ErrorResponseDto> handleEmailNotVerifiedException(EmailNotVerifiedException e) {
        log.error("handleEmailNotVerifiedException", e);
        ErrorResponseDto error = new ErrorResponseDto(e.getErrorCode());

        return new ResponseEntity<>(error, HttpStatus.valueOf(e.getErrorCode().getStatus()));
    }

    @ExceptionHandler(EmailDuplicateException.class)
    public ResponseEntity<ErrorResponseDto> handleEmailDuplicateException(EmailDuplicateException e) {
        log.error("handleEmailDuplicateException", e);
        ErrorResponseDto error = new ErrorResponseDto(e.getErrorCode());

        return new ResponseEntity<>(error, HttpStatus.valueOf(e.getErrorCode().getStatus()));
    }

    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<ErrorResponseDto> handlePasswordMismatchException(PasswordMismatchException e) {
        log.error("handlePasswordMismatchException", e);
        ErrorResponseDto error = new ErrorResponseDto(e.getErrorCode());

        return new ResponseEntity<>(error, HttpStatus.valueOf(e.getErrorCode().getStatus()));
    }

    @ExceptionHandler(PasswordSameException.class)
    public ResponseEntity<ErrorResponseDto> handlePasswordSameException(PasswordSameException e) {
        log.error("handlePasswordSameException", e);
        ErrorResponseDto error = new ErrorResponseDto(e.getErrorCode());

        return new ResponseEntity<>(error, HttpStatus.valueOf(e.getErrorCode().getStatus()));
    }

    @ExceptionHandler(PerfumeNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handlePerfumeNotFoundException(PerfumeNotFoundException e) {
        log.error("handlePerfumeNotFoundException", e);
        ErrorResponseDto error = new ErrorResponseDto(e.getErrorCode());

        return new ResponseEntity<>(error, HttpStatus.valueOf(e.getErrorCode().getStatus()));
    }

    @ExceptionHandler(NotificationNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleNotificationNotFoundException(NotificationNotFoundException e) {
        log.error("handleNotificationNotFoundException", e);
        ErrorResponseDto error = new ErrorResponseDto(e.getErrorCode());

        return new ResponseEntity<>(error, HttpStatus.valueOf(e.getErrorCode().getStatus()));
    }

    @ExceptionHandler(NotificationSendFailException.class)
    public ResponseEntity<ErrorResponseDto> handleNotificationSendFailException(NotificationSendFailException e) {
        log.error("handleNotificationSendFailException", e);
        ErrorResponseDto error = new ErrorResponseDto(e.getErrorCode());

        return new ResponseEntity<>(error, HttpStatus.valueOf(e.getErrorCode().getStatus()));
    }

    @ExceptionHandler(ChatroomNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleChatroomNotFoundException(ChatroomNotFoundException e) {
        log.error("handleChatroomNotFoundException", e);
        ErrorResponseDto error = new ErrorResponseDto(e.getErrorCode());

        return new ResponseEntity<>(error, HttpStatus.valueOf(e.getErrorCode().getStatus()));
    }

    @ExceptionHandler(ChatroomPermissionException.class)
    public ResponseEntity<ErrorResponseDto> handleChatroomPermissionException(ChatroomPermissionException e) {
        log.error("handleChatroomPermissionException", e);
        ErrorResponseDto error = new ErrorResponseDto(e.getErrorCode());

        return new ResponseEntity<>(error, HttpStatus.valueOf(e.getErrorCode().getStatus()));
    }
}
