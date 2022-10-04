package colfume.common;

import colfume.util.CookieUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class HomeController {

    @GetMapping("/")
    public String home(HttpServletRequest request, HttpServletResponse response, Model model) {
        Optional<Cookie> accessToken = CookieUtils.getCookie(request, "access-token");
        if (accessToken.isEmpty()) {
            return "home";
        }
        return "login-home";
    }
}