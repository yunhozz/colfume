package colfume.dto;

import colfume.domain.member.model.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class MemberDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberRequestDto {

        @NotBlank(message = "이메일을 입력해주세요.")
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        private String email;

        @NotBlank(message = "비밀번호를 입력해주세요.")
        private String password;

        @NotBlank(message = "이름을 입력해주세요.")
        private String name;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginRequestDto {

        @NotBlank(message = "이메일을 입력해주세요.")
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        private String email;

        @NotBlank(message = "비밀번호를 입력해주세요.")
        private String password;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PasswordRequestDto {

        @NotBlank(message = "기존 비밃번호를 입력해주세요.")
        private String password;

        @NotBlank(message = "변경하실 비밀번호를 입력해주세요.")
        private String newPw;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberResponseDto {

        private Long id;
        private String email;
        private String password;
        private String name;
        private LocalDateTime createdDate;
        private LocalDateTime lastModifiedDate;

        public MemberResponseDto(Member member) {
            id = member.getId();
            email = member.getEmail();
            password = member.getPassword();
            name = member.getName();
            createdDate = member.getCreatedDate();
            lastModifiedDate = member.getLastModifiedDate();
        }
    }
}
