package colfume.oauth.jwt;

import colfume.common.dto.ErrorResponseDto;
import colfume.common.enums.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    // 서버에 요청을 할 때 액세스가 가능한지 권한을 체크 후 액세스 할 수 없는 요청을 했을시 동작

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.sendError(HttpServletResponse.SC_FORBIDDEN);

        ErrorResponseDto errorResponseDto = new ErrorResponseDto(ErrorCode.FORBIDDEN);
        ObjectMapper objectMapper = new ObjectMapper();
        String error = objectMapper.writeValueAsString(errorResponseDto);
        log.error("error = " + error);
    }
}