package colfume.oauth;

import colfume.domain.member.service.dto.TokenResponseDto;
import colfume.oauth.jwt.JwtProvider;
import colfume.oauth.jwt.UserRefreshToken;
import colfume.oauth.jwt.UserRefreshTokenRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;
    private final ObjectMapper objectMapper;
    private final UserRefreshTokenRepository userRefreshTokenRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        TokenResponseDto tokenDto = jwtProvider.createTokenDto(userPrincipal.getEmail(), userPrincipal.getRole());

        saveOrUpdateRefreshToken(userPrincipal, tokenDto);
        writeTokenResponse(response, tokenDto);
        response.sendRedirect("/me");
    }

    private void saveOrUpdateRefreshToken(UserPrincipal userPrincipal, TokenResponseDto tokenDto) {
        Optional<UserRefreshToken> userRefreshTokenOptional = userRefreshTokenRepository.findByUserId(userPrincipal.getId());
        UserRefreshToken userRefreshToken;

        if (userRefreshTokenOptional.isEmpty()) {
            userRefreshToken = new UserRefreshToken(userPrincipal.getId(), tokenDto.getRefreshToken());
            userRefreshTokenRepository.save(userRefreshToken);

        } else {
            userRefreshToken = userRefreshTokenOptional.get();
            userRefreshToken.updateRefreshToken(tokenDto.getRefreshToken());
        }
    }

    private void writeTokenResponse(HttpServletResponse response, TokenResponseDto tokenResponseDto) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        response.addHeader("Authentication", tokenResponseDto.getAccessToken());
        response.addHeader("Refresh", tokenResponseDto.getRefreshToken());

        PrintWriter writer = response.getWriter();
        writer.println(objectMapper.writeValueAsString(tokenResponseDto));
        writer.flush();
    }
}