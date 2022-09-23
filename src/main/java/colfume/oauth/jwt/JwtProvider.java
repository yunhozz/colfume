package colfume.oauth.jwt;

import colfume.domain.member.model.entity.MemberAuthority;
import colfume.oauth.UserDetailsServiceImpl;
import colfume.dto.TokenResponseDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.Base64UrlCodec;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtProvider {

    @Value("${colfume.jwt.secret}")
    private String secretKey;

    private final String ROLES = "roles";
    private final Long ACCESSTOKEN_VALID_MILLISECOND = 60 * 60 * 1000L; // 1 hour
    private final Long REFRESHTOKEN_VALID_MILLISECOND = 14 * 24 * 60 * 60 * 1000L; // 14 day
    private final UserDetailsServiceImpl userDetailsService;

    @PostConstruct
    protected void init() {
        secretKey = Base64UrlCodec.BASE64URL.encode(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public TokenResponseDto createTokenDto(String email, Set<MemberAuthority> memberAuthorities) {
        List<String> roles = memberAuthorities.stream()
                .map(memberAuthority -> memberAuthority.getAuthority().getRole()).toList();
        Claims claims = Jwts.claims().setSubject(email);
        claims.put(ROLES, roles);
        Date now = new Date();

        // resource 에 직접 접근할 수 있는 필수적인 정보를 담은 토큰, 짧은 생명 주기
        String accessToken = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + ACCESSTOKEN_VALID_MILLISECOND))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        // access token 을 재발급 받기 위한 정보를 가지는 토큰, 긴 생명 주기
        String refreshToken = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(claims)
                .setExpiration(new Date(now.getTime() + REFRESHTOKEN_VALID_MILLISECOND))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        return TokenResponseDto.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpireDate(ACCESSTOKEN_VALID_MILLISECOND)
                .build();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
        log.info("member email = {}", claims.getSubject());
        UserDetails userDetails = userDetailsService.loadUserByUsername(claims.getSubject());

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("X-AUTH-TOKEN");
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.error("잘못된 Jwt 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.error("만료된 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("지원하지 않는 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.error("잘못된 토큰입니다.");
        }
        return false;
    }
}