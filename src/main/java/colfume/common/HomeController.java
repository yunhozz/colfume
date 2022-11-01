package colfume.common;

import colfume.oauth.UserPrincipal;
import colfume.oauth.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final JwtProvider jwtProvider;

    @GetMapping("/")
    public String home(@RequestParam(required = false) String token, Model model) {
        if (token != null) {
            Authentication authentication = jwtProvider.getAuthentication(token);
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            model.addAttribute("principal", userPrincipal);

            return "login-home";
        }
        return "home";
    }
}