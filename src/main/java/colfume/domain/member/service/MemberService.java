package colfume.domain.member.service;

import colfume.api.dto.member.MemberRequestDto;
import colfume.api.dto.member.TokenRequestDto;
import colfume.common.enums.ErrorCode;
import colfume.oauth.jwt.JwtProvider;
import colfume.oauth.jwt.UserRefreshToken;
import colfume.oauth.jwt.UserRefreshTokenRepository;
import colfume.domain.member.model.entity.Authority;
import colfume.domain.member.model.entity.Member;
import colfume.domain.member.model.entity.MemberAuthority;
import colfume.domain.member.model.repository.AuthorityRepository;
import colfume.domain.member.model.repository.MemberAuthorityRepository;
import colfume.domain.member.model.repository.MemberRepository;
import colfume.domain.member.service.dto.MemberResponseDto;
import colfume.domain.member.service.dto.TokenResponseDto;
import colfume.domain.member.service.exception.EmailDuplicateException;
import colfume.domain.member.service.exception.EmailNotFoundException;
import colfume.domain.member.service.exception.EmailNotVerifiedException;
import colfume.domain.member.service.exception.MemberNotFoundException;
import colfume.domain.member.service.exception.PasswordMismatchException;
import colfume.domain.member.service.exception.PasswordSameException;
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
    private final AuthorityRepository authorityRepository;
    private final MemberAuthorityRepository memberAuthorityRepository;
    private final UserRefreshTokenRepository userRefreshTokenRepository;
    private final JwtProvider jwtProvider;
    private final BCryptPasswordEncoder encoder;

    @Transactional
    public Long join(MemberRequestDto memberRequestDto) {
        if (memberRepository.findAll().stream().anyMatch(m -> m.getEmail().equals(memberRequestDto.getEmail()))) {
            throw new EmailDuplicateException(ErrorCode.EMAIL_DUPLICATE);
        }
        Member member = Member.builder()
                .email(memberRequestDto.getEmail())
                .password(encoder.encode(memberRequestDto.getPassword()))
                .name(memberRequestDto.getName())
                .imageUrl(memberRequestDto.getImageUrl())
                .build();

        Authority role_user = authorityRepository.getReferenceById("ROLE_USER");
        memberAuthorityRepository.save(new MemberAuthority(member, role_user));

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
        TokenResponseDto tokenDto = jwtProvider.createTokenDto(email, member.getMemberAuthorities());
        userRefreshTokenRepository.save(new UserRefreshToken(member.getId(), tokenDto.getRefreshToken()));

        return tokenDto;
    }

    // access token 재발급
    // TODO : 예외 처리 수정
    @Transactional
    public TokenResponseDto tokenReissue(TokenRequestDto tokenRequestDto) {
        if (!jwtProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new IllegalStateException("검증되지 않은 jwt 토큰입니다.");
        }
        Authentication authentication = jwtProvider.getAuthentication(tokenRequestDto.getAccessToken());
        Member member = findMemberByEmail(authentication.getName());
        UserRefreshToken userRefreshToken = userRefreshTokenRepository.findByUserId(member.getId())
                .orElseThrow(() -> new IllegalStateException("재발급 jwt 토큰을 찾을 수 없습니다."));

        if (!userRefreshToken.getRefreshToken().equals(tokenRequestDto.getRefreshToken())) {
            throw new IllegalStateException("재발급 jwt 토큰이 일치하지 않습니다.");
        }
        TokenResponseDto tokenDto = jwtProvider.createTokenDto(member.getEmail(), member.getMemberAuthorities());
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
        return new MemberResponseDto(findMember(userId));
    }

    @Transactional(readOnly = true)
    public List<MemberResponseDto> findMemberDtoList() {
        return memberRepository.findAll().stream()
                .map(MemberResponseDto::new)
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