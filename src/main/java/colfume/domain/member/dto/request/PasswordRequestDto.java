package colfume.domain.member.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PasswordRequestDto {

    @NotBlank(message = "기존 비밃번호를 입력해주세요.")
    private String password;

    @NotBlank(message = "변경하실 비밀번호를 입력해주세요.")
    private String newPw;
}