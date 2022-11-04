package colfume.domain.member.service;

import colfume.api.dto.member.MemberRequestDto;
import colfume.common.converter.entity.MemberConverter;
import colfume.common.enums.ErrorCode;
import colfume.domain.member.model.entity.Member;
import colfume.domain.member.model.repository.MemberRepository;
import colfume.domain.member.service.dto.MemberResponseDto;
import colfume.domain.member.service.dto.TokenResponseDto;
import colfume.domain.member.service.exception.EmailDuplicateException;
import colfume.domain.member.service.exception.EmailNotFoundException;
import colfume.domain.member.service.exception.EmailNotVerifiedException;
import colfume.domain.member.service.exception.MemberNotFoundException;
import colfume.domain.member.service.exception.PasswordMismatchException;
import colfume.domain.member.service.exception.PasswordSameException;
import colfume.domain.member.service.exception.RefreshTokenNotCorrespondException;
import colfume.domain.member.service.exception.RefreshTokenNotFoundException;
import colfume.oauth.jwt.JwtProvider;
import colfume.oauth.model.UserPrincipal;
import colfume.oauth.model.UserRefreshToken;
import colfume.oauth.model.UserRefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final UserRefreshTokenRepository userRefreshTokenRepository;
    private final JwtProvider jwtProvider;
    private final MemberConverter converter;
    private final BCryptPasswordEncoder encoder;

    @Transactional
    public Long join(MemberRequestDto memberRequestDto) {
        if (memberRepository.findAll().stream().anyMatch(m -> m.getEmail().equals(memberRequestDto.getEmail()))) {
            throw new EmailDuplicateException(ErrorCode.EMAIL_DUPLICATE);
        }
        Member member = converter.convertToEntity(memberRequestDto);

        return memberRepository.save(member).getId();
    }

    @Transactional
    public TokenResponseDto login(String email, String password) {
        Member member = findMemberByEmail(email);

        if (!encoder.matches(password, member.getPassword())) {
            throw new PasswordMismatchException(ErrorCode.PASSWORD_MISMATCH);
        }
        if (!member.isEmailVerified()) {
            throw new EmailNotVerifiedException(ErrorCode.EMAIL_NOT_VERIFIED);
        }
        TokenResponseDto tokenDto = jwtProvider.createTokenDto(email, member.getRole().getAuthority());
        userRefreshTokenRepository.save(new UserRefreshToken(member.getId(), tokenDto.getRefreshToken()));

        return tokenDto;
    }

    // jwt token 재발급
    @Transactional
    public TokenResponseDto tokenReissue(String refreshToken) {
        Authentication authentication = jwtProvider.getAuthentication(refreshToken);
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Member member = findMemberByEmail(userPrincipal.getEmail());
        UserRefreshToken userRefreshToken = userRefreshTokenRepository.findByUserId(member.getId())
                .orElseThrow(() -> new RefreshTokenNotFoundException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));

        if (!userRefreshToken.getRefreshToken().equals(refreshToken)) {
            throw new RefreshTokenNotCorrespondException(ErrorCode.REFRESH_TOKEN_NOT_CORRESPOND);
        }
        TokenResponseDto tokenDto = jwtProvider.createTokenDto(member.getEmail(), member.getRole().getAuthority());
        userRefreshToken.updateRefreshToken(tokenDto.getRefreshToken());

        return tokenDto;
    }

    @Transactional
    public void updatePassword(Long userId, String password, String newPw) {
        if (password.equals(newPw)) {
            throw new PasswordSameException(ErrorCode.PASSWORD_SAME);
        }
        Member member = findMember(userId);

        if (!encoder.matches(password, member.getPassword())) {
            throw new PasswordMismatchException(ErrorCode.PASSWORD_MISMATCH);
        }
        member.updatePassword(encoder.encode(newPw));
    }

    @Transactional
    public void updateInfo(Long userId, String name, String imageUrl) {
        Member member = findMember(userId);
        member.updateInfo(name, imageUrl);
    }

    @Transactional
    public void withdraw(Long userId) {
        Member member = findMember(userId);
        memberRepository.delete(member);
    }

    @Transactional(readOnly = true)
    public MemberResponseDto findMemberDto(Long userId) {
        Member member = findMember(userId);
        return converter.convertToDto(member);
    }

    @Transactional(readOnly = true)
    public List<MemberResponseDto> findMemberDtoList() {
        return memberRepository.findAll().stream()
                .map(converter::convertToDto)
                .collect(Collectors.toList());
    }

    private Member findMember(Long userId) {
        return memberRepository.findById(userId)
                .orElseThrow(() -> new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND));
    }

    private Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new EmailNotFoundException(ErrorCode.EMAIL_NOT_FOUND));
    }
}