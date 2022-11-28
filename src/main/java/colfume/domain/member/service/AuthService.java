package colfume.domain.member.service;

import colfume.common.dto.TokenResponseDto;
import colfume.domain.member.model.entity.Member;
import colfume.domain.member.model.repository.MemberRepository;
import colfume.domain.member.service.exception.EmailNotFoundException;
import colfume.domain.member.service.exception.PasswordMismatchException;
import colfume.domain.member.service.exception.RefreshTokenNotCorrespondException;
import colfume.domain.member.service.exception.RefreshTokenNotFoundException;
import colfume.oauth.jwt.JwtProvider;
import colfume.oauth.model.UserPrincipal;
import colfume.oauth.model.UserRefreshToken;
import colfume.oauth.model.UserRefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final UserRefreshTokenRepository userRefreshTokenRepository;
    private final JwtProvider jwtProvider;

    @Transactional
    public TokenResponseDto login(String email, String password) {
        Member member = findMemberByEmail(email);

        if (member.isPasswordNotEqualsWith(password)) {
            throw new PasswordMismatchException();
        }

        TokenResponseDto tokenResponseDto = jwtProvider.createTokenDto(email, member.getRole().getAuthority());
        userRefreshTokenRepository.save(new UserRefreshToken(member.getId(), tokenResponseDto.getRefreshToken()));

        return tokenResponseDto;
    }

    // jwt token 재발급
    @Transactional
    public TokenResponseDto tokenReissue(String refreshToken) {
        Authentication authentication = jwtProvider.getAuthentication(refreshToken);
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UserRefreshToken userRefreshToken = userRefreshTokenRepository.findByUserId(userPrincipal.getId())
                .orElseThrow(RefreshTokenNotFoundException::new);

        if (userRefreshToken.isRefreshTokenNotEqualsWith(refreshToken)) {
            throw new RefreshTokenNotCorrespondException();
        }

        TokenResponseDto tokenResponseDto = jwtProvider.createTokenDto(userPrincipal.getEmail(), userPrincipal.getRole());
        userRefreshToken.updateRefreshToken(tokenResponseDto.getRefreshToken());

        return tokenResponseDto;
    }

    private Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(EmailNotFoundException::new);
    }
}