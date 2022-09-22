package colfume.domain.member.service;

import colfume.domain.member.model.entity.*;
import colfume.domain.member.model.repository.AuthorityRepository;
import colfume.domain.member.model.repository.MailCodeRepository;
import colfume.domain.member.model.repository.MemberAuthorityRepository;
import colfume.domain.member.model.repository.MemberRepository;
import colfume.dto.TokenResponseDto;
import colfume.enums.ErrorCode;
import colfume.exception.*;
import colfume.util.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static colfume.dto.MemberDto.*;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final AuthorityRepository authorityRepository;
    private final MemberAuthorityRepository memberAuthorityRepository;
    private final ConfirmationTokenService confirmationTokenService;
    private final MailCodeRepository mailCodeRepository;
    private final JwtProvider jwtProvider;
    private final BCryptPasswordEncoder encoder;

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

    // 회원가입 완료 후 링크를 통해 인증
    public void confirmEmail(Long tokenId) {
        ConfirmationToken confirmationToken = confirmationTokenService.findByIdAndExpirationDateAfterAndExpired(tokenId);
        Member member = findMember(confirmationToken.getUserId());

        confirmationToken.useToken(); // 토큰 만료
        member.emailVerified(); // 이메일 인증 성공
    }

    // 회원가입 도중 이메일로 전송된 코드로 인증
    public void confirmCode(String email, String code) {
        MailCode mailCode = mailCodeRepository.findByEmail(email)
                .orElseThrow(() -> new EmailNotFoundException(ErrorCode.EMAIL_NOT_FOUND));
        if (!mailCode.getCode().equals(code)) {
            throw new EmailNotVerifiedException(ErrorCode.EMAIL_NOT_VERIFIED);
        }
        mailCode.verified(); // 코드 검증 성공
    }

    public TokenResponseDto login(String email, String password) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new EmailNotFoundException(ErrorCode.EMAIL_NOT_FOUND));
        if (!encoder.matches(password, member.getPassword())) {
            throw new PasswordMismatchException(ErrorCode.PASSWORD_MISMATCH);
        }
        if (!member.isEmailVerified()) {
            throw new EmailNotVerifiedException(ErrorCode.EMAIL_NOT_VERIFIED);
        }
        return jwtProvider.createTokenDto(email, member.getMemberAuthorities());
    }

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

    public void updateInfo(Long userId, String name, String imageUrl) {
        Member member = findMember(userId);
        member.updateInfo(name, imageUrl);
    }

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

    @Transactional(readOnly = true)
    private Member findMember(Long userId) {
        return memberRepository.findById(userId)
                .orElseThrow(() -> new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND));
    }
}