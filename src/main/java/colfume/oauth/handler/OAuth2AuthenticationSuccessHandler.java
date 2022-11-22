package colfume.oauth.handler;

import colfume.common.util.CookieUtils;
import colfume.common.dto.TokenResponseDto;
import colfume.oauth.model.UserPrincipal;
import colfume.oauth.jwt.JwtProvider;
import colfume.oauth.model.UserRefreshToken;
import colfume.oauth.model.UserRefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

import static colfume.oauth.handler.OAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;
import static colfume.oauth.handler.OAuth2AuthorizationRequestRepository.REFRESH_TOKEN;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;
    private final UserRefreshTokenRepository userRefreshTokenRepository;
    private final OAuth2AuthorizationRequestRepository oAuth2AuthorizationRequestRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        String targetUri = determineTargetUrl(request, response, authentication);

        if (response.isCommitted()) {
            log.debug("Response has already been committed. Unable to redirect to " + targetUri);
            return;
        }
        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUri);
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String[] targetUri = {null};
        CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue)
                .ifPresentOrElse(redirectUri -> {
                    throw new IllegalStateException("Sorry! We've got an Unauthorized Redirect URI and can't proceed with the authentication");
                }, () -> {
                    targetUri[0] = getDefaultTargetUrl();
                });

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        TokenResponseDto tokenResponseDto = jwtProvider.createTokenDto(userPrincipal.getEmail(), userPrincipal.getRole());

        saveOrUpdateRefreshToken(userPrincipal, tokenResponseDto);
        addTokenOnResponseAndCookie(request, response, tokenResponseDto);

        // 테스트를 위한 세션 생성 후 토큰 저장
        HttpSession session = request.getSession();
        session.setAttribute("token", tokenResponseDto.getAccessToken());

        return UriComponentsBuilder.fromUriString(targetUri[0])
                .queryParam("token", tokenResponseDto.getAccessToken())
                .build().toUriString();
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        oAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    @Transactional
    private void saveOrUpdateRefreshToken(UserPrincipal userPrincipal, TokenResponseDto tokenResponseDto) {
        Optional<UserRefreshToken> optionalUserRefreshToken = userRefreshTokenRepository.findByUserId(userPrincipal.getId());

        if (optionalUserRefreshToken.isEmpty()) {
            UserRefreshToken userRefreshToken = new UserRefreshToken(userPrincipal.getId(), tokenResponseDto.getRefreshToken());
            userRefreshTokenRepository.save(userRefreshToken);

        } else {
            UserRefreshToken userRefreshToken = optionalUserRefreshToken.get();
            userRefreshToken.updateRefreshToken(tokenResponseDto.getRefreshToken());
        }
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