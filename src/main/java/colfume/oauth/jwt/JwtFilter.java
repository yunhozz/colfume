package colfume.oauth.jwt;

import io.jsonwebtoken.lang.Strings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("[Verifying token]");
        log.info("url = " + request.getRequestURL().toString());

        resolveToken(request).ifPresent(token -> {
            if (jwtProvider.validateToken(token)) {
                Authentication authentication = jwtProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        });

        filterChain.doFilter(request, response);
    }

    private Optional<String> resolveToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        log.info("jwt token = " + token);

        if (!Strings.hasText(token)) {
            return Optional.empty();
        }
        return checkParts(token);
    }

    private Optional<String> checkParts(String token) {
        String[] parts = token.split(" ");

        if (parts.length == 2 && parts[0].equals("Bearer")) {
            return Optional.ofNullable(parts[1]);
        }
        return Optional.empty();
    }
}