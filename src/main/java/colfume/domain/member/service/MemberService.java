package colfume.domain.member.service;

import colfume.domain.member.model.entity.Authority;
import colfume.domain.member.model.entity.Member;
import colfume.domain.member.model.entity.MemberAuthority;
import colfume.domain.member.model.repository.AuthorityRepository;
import colfume.domain.member.model.repository.MemberAuthorityRepository;
import colfume.domain.member.model.repository.MemberRepository;
import colfume.dto.TokenResponseDto;
import colfume.enums.ErrorCode;
import colfume.exception.EmailDuplicateException;
import colfume.exception.MemberNotFoundException;
import colfume.exception.PasswordMismatchException;
import colfume.util.JwtProvider;
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
    private final JwtProvider jwtProvider;
    private final BCryptPasswordEncoder encoder;

    public Long join(MemberRequestDto memberDto) {
        if (memberRepository.findAll().stream().anyMatch(m -> m.getEmail().equals(memberDto.getEmail()))) {
            throw new EmailDuplicateException(ErrorCode.EMAIL_DUPLICATE);
        }
        Member member = Member.builder()
                .email(memberRequestDto.getEmail())
                .password(encoder.encode(memberRequestDto.getPassword()))
                .name(memberRequestDto.getName())
                .build();

        Authority role_user = authorityRepository.getReferenceById("ROLE_USER");
        memberAuthorityRepository.save(new MemberAuthority(member, role_user));

        return memberRepository.save(member).getId();
    }

    public TokenResponseDto login(LoginRequestDto loginRequestDto) {
        Member member = memberRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND));
        if (!encoder.matches(loginRequestDto.getPassword(), member.getPassword())) {
            throw new PasswordMismatchException(ErrorCode.PASSWORD_MISMATCH);
        }
        return jwtProvider.createTokenDto(loginRequestDto.getEmail(), member.getMemberAuthorities());
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
