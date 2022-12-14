package colfume.domain.member.service;

import colfume.domain.member.dto.request.LoginRequestDto;
import colfume.domain.member.dto.request.MemberRequestDto;
import colfume.common.enums.ErrorCode;
import colfume.domain.member.model.entity.Member;
import colfume.domain.member.model.repository.MemberRepository;
import colfume.common.dto.TokenResponseDto;
import colfume.domain.member.service.exception.EmailDuplicateException;
import colfume.domain.member.service.exception.EmailNotFoundException;
import colfume.domain.member.service.exception.PasswordMismatchException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    AuthService authService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    BCryptPasswordEncoder encoder;

    @BeforeEach
    public void beforeEach() {
        Member member = createMember("email@gmail.com");
        memberRepository.save(member);
    }

    @Test
    void join() throws Exception {
        //given
        MemberRequestDto memberDto = new MemberRequestDto("qkrdbsgh@gmail.com", "123", "yunho", null);

        //when
        Long userId = memberService.join(memberDto);

        //then
        assertThat(userId).isNotNull();
    }

    @Test
    void join_fail_email_duplicate() throws Exception {
        //given
        MemberRequestDto memberDto1 = new MemberRequestDto("qkrdbsgh@gmail.com", "123", "yunho", null);
        MemberRequestDto memberDto2 = new MemberRequestDto("qkrdbsgh@gmail.com", "123", "yunho", null);

        //when
        try {
            memberService.join(memberDto1);
            memberService.join(memberDto2);
        } catch (EmailDuplicateException e) {
            assertThat(e.getErrorCode()).isEqualTo(ErrorCode.EMAIL_DUPLICATE);
        }
    }

    @Test
    void login() throws Exception {
        //given
        LoginRequestDto loginDto = new LoginRequestDto("email@gmail.com", "123");

        //when
        TokenResponseDto result = authService.login(loginDto.getEmail(), loginDto.getPassword());

        //then
        assertThat(result).isNotNull();
        assertThat(result.getGrantType()).isEqualTo("Bearer");
    }

    @Test
    void login_fail_email_not_found() throws Exception {
        //given
        LoginRequestDto loginDto = new LoginRequestDto("error@gmail.com", "123");

        //when
        try {
            authService.login(loginDto.getEmail(), loginDto.getPassword());
        } catch (EmailNotFoundException e) {
            assertThat(e.getErrorCode()).isEqualTo(ErrorCode.EMAIL_NOT_FOUND);
        }
    }

    @Test
    void login_fail_password_mismatch() throws Exception {
        //given
        LoginRequestDto loginDto = new LoginRequestDto("email@gmail.com", "error");

        //when
        try {
            authService.login(loginDto.getEmail(), loginDto.getPassword());
        } catch (PasswordMismatchException e) {
            assertThat(e.getErrorCode()).isEqualTo(ErrorCode.PASSWORD_MISMATCH);
        }
    }

    private Member createMember(String email) {
        return Member.builder()
                .email(email)
                .password(encoder.encode("123"))
                .name("tester")
                .build();
    }
}