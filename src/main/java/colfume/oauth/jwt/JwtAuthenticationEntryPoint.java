package colfume.oauth.jwt;

import colfume.common.dto.ErrorResponseDto;
import colfume.common.enums.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    // 인증이 되지않은 유저가 요청을 했을 때 동작

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);

        ErrorResponseDto errorResponseDto = new ErrorResponseDto(ErrorCode.UNAUTHORIZED);
        ObjectMapper objectMapper = new ObjectMapper();
        String error = objectMapper.writeValueAsString(errorResponseDto);
        log.error("error = " + error);
    }
}