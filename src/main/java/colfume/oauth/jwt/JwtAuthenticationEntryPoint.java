package colfume.oauth.jwt;

import colfume.oauth.model.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
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
        log.error("handleAuthenticationException", e);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED); // 401
        response.setCharacterEncoding("UTF-8");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        if (userPrincipal != null) {
            request.setAttribute("email", userPrincipal.getUsername());
            request.setAttribute("name", userPrincipal.getName());
            request.setAttribute("nextPage", "/");
            request.setAttribute("msg", e.getMessage());
        }

        request.getRequestDispatcher("/err/401").forward(request, response);
    }
}