package colfume.domain.member.controller;

import colfume.common.dto.ErrorResponseDto;
import colfume.common.dto.Response;
import colfume.common.dto.TokenResponseDto;
import colfume.common.enums.ErrorCode;
import colfume.common.util.CookieUtils;
import colfume.domain.member.dto.request.LoginRequestDto;
import colfume.domain.member.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Optional;

import static colfume.oauth.handler.OAuth2AuthorizationRequestRepository.REFRESH_TOKEN;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthApiController {

    private final AuthService authService;

    @PostMapping("/login")
    public Response login(@Valid @RequestBody LoginRequestDto loginRequestDto, HttpServletRequest request, HttpServletResponse response) {
        TokenResponseDto tokenResponseDto = authService.login(loginRequestDto.getEmail(), loginRequestDto.getPassword());
        addTokenOnResponseAndCookie(request, response, tokenResponseDto);

        return Response.success(tokenResponseDto, HttpStatus.CREATED);
    }

    @PostMapping("/token")
    public Response tokenReissue(HttpServletRequest request, HttpServletResponse response) {
        Optional<Cookie> cookie = CookieUtils.getCookie(request, REFRESH_TOKEN);

        if (cookie.isEmpty()) {
            ErrorResponseDto error = new ErrorResponseDto(ErrorCode.COOKIE_NOT_FOUND);
            return Response.failure(-1000, error, HttpStatus.valueOf(error.getStatus()));
        }

        String refreshToken = cookie.get().getValue();
        TokenResponseDto tokenResponseDto = authService.tokenReissue(refreshToken);
        addTokenOnResponseAndCookie(request, response, tokenResponseDto);

        return Response.success(tokenResponseDto, HttpStatus.OK);
    }

    private void addTokenOnResponseAndCookie(HttpServletRequest request, HttpServletResponse response, TokenResponseDto tokenResponseDto) {
        addAccessTokenOnResponse(response, tokenResponseDto);
        addRefreshTokenOnCookie(request, response, tokenResponseDto);
    }

    // 헤더에 jwt access 토큰 추가
    private void addAccessTokenOnResponse(HttpServletResponse response, TokenResponseDto tokenResponseDto) {
        response.setContentType("application/json;charset=UTF-8");
        response.addHeader("Authorization", tokenResponseDto.getGrantType() + tokenResponseDto.getAccessToken());
    }

    // 쿠키에 jwt refresh 토큰 추가
    private void addRefreshTokenOnCookie(HttpServletRequest request, HttpServletResponse response, TokenResponseDto tokenResponseDto) {
        CookieUtils.deleteCookie(request, response, REFRESH_TOKEN);
        CookieUtils.addCookie(response, REFRESH_TOKEN, tokenResponseDto.getRefreshToken(), (int) tokenResponseDto.getRefreshTokenExpireDate() / 3600);
    }
}