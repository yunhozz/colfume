package colfume.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotValidResponseDto {

    private String message;
    private String field;
    private Object rejectValue;
    private String code;
}
