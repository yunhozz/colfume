package colfume.common;

import colfume.oauth.jwt.JwtProvider;
import colfume.oauth.model.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final JwtProvider jwtProvider;

    @GetMapping("/")
    public String home(HttpServletRequest request, Model model) {
//        String token = request.getHeader("Authentication").split(" ")[1];

        // 테스트를 위한 세션 생성 후 토큰 가져오기
        HttpSession session = request.getSession();
        String token = (String) session.getAttribute("token");

        if (token != null) {
            Authentication authentication = jwtProvider.getAuthentication(token);
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            model.addAttribute("principal", userPrincipal);

            return "login-home";
        }
        return "home";
    }
}