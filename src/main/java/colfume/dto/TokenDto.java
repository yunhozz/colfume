package colfume.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class TokenDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TokenRequestDto {

        private String accessToken;
        private String refreshToken;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TokenResponseDto {

        private String grantType;
        private String accessToken;
        private String refreshToken;
        private Long accessTokenExpireDate;
    }
}