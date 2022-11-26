package colfume.oauth.jwt;

import colfume.oauth.model.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
        log.error("handleAccessDeniedException", e);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.sendError(HttpServletResponse.SC_FORBIDDEN); // 403
        response.setCharacterEncoding("UTF-8");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        if (userPrincipal != null) {
            request.setAttribute("email", userPrincipal.getUsername());
            request.setAttribute("name", userPrincipal.getName());
            request.setAttribute("nextPage", "/");
            request.setAttribute("msg", e.getMessage());
        }

        request.getRequestDispatcher("/err/403").forward(request, response);
    }
}