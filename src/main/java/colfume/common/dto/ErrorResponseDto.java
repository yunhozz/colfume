package colfume.common.dto;

import colfume.common.enums.ErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponseDto {

    private Integer status;
    private String code;
    private String message;
    private List<NotValidResponseDto> fieldErrors;

    public ErrorResponseDto(ErrorCode errorCode) {
        status = errorCode.getStatus();
        code = errorCode.getCode();
        message = errorCode.getMessage();
    }

    public ErrorResponseDto(ErrorCode errorCode, List<NotValidResponseDto> fieldErrors) {
        status = errorCode.getStatus();
        code = errorCode.getCode();
        message = errorCode.getMessage();
        this.fieldErrors = fieldErrors;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NotValidResponseDto {

        private String field;
        private String code;
        private Object rejectValue;
        private String defaultMessage;
    }
}
