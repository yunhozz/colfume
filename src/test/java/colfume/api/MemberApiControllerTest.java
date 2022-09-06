package colfume.api;

import colfume.domain.member.model.entity.Member;
import colfume.domain.member.model.repository.MemberRepository;
import colfume.domain.member.service.MemberService;
import colfume.dto.TokenResponseDto;
import colfume.util.JwtProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static colfume.dto.MemberDto.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD) // 데이터를 지우고 auto increment 초기화
class MemberApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Test
    @WithMockUser
    @DisplayName("GET /api/members/{memberId}")
    void getMember() throws Exception {
        //given
        Member member = createMember("email@gmail.com");
        memberRepository.save(member);

        //when
        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.get("/api/members/{memberId}", member.getId())
                .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("id").value(member.getId()))
                .andExpect(jsonPath("email").value("email@gmail.com"))
                .andExpect(jsonPath("password").exists())
                .andExpect(jsonPath("name").value("tester"));
    }

    @Test
    @WithMockUser
    @DisplayName("GET /api/members")
    void getMembers() throws Exception {
        //given
        Member member1 = createMember("email1@gmail.com");
        Member member2 = createMember("email2@gmail.com");
        Member member3 = createMember("email3@gmail.com");
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        //when
        ResultActions result = mockMvc.perform(get("/api/members")
                .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(member1.getId()))
                .andExpect(jsonPath("$[0].email").value("email1@gmail.com"))
                .andExpect(jsonPath("$[0].password").exists())
                .andExpect(jsonPath("$[0].name").value("tester"))
                .andExpect(jsonPath("$[1].id").value(member2.getId()))
                .andExpect(jsonPath("$[1].email").value("email2@gmail.com"))
                .andExpect(jsonPath("$[1].password").exists())
                .andExpect(jsonPath("$[1].name").value("tester"))
                .andExpect(jsonPath("$[2].id").value(member3.getId()))
                .andExpect(jsonPath("$[2].email").value("email3@gmail.com"))
                .andExpect(jsonPath("$[2].password").exists())
                .andExpect(jsonPath("$[2].name").value("tester"));
    }

    @Test
    @Commit
    @WithMockUser
    @DisplayName("POST /api/members/join")
    void join() throws Exception {
        //given
        MemberRequestDto memberRequestDto = new MemberRequestDto("test@gmail.com", "123", "tester");

        //when
        ResultActions result = mockMvc.perform(post("/api/members/join")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberRequestDto))
        );

        //then
        result.andExpect(status().isCreated())
                .andExpect(content().string("1"));
    }

    @Test
    @WithMockUser
    @DisplayName("POST /api/members/login")
    void login() throws Exception {
        //given
        Member member1 = createMember("email1@gmail.com");
        Member member2 = createMember("email2@gmail.com");
        Member member3 = createMember("email3@gmail.com");
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        LoginRequestDto loginRequestDto = new LoginRequestDto("email1@gmail.com", "123");

        //when
        ResultActions result = mockMvc.perform(post("/api/members/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequestDto))
        );

        //then
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("grantType").value("Bearer"))
                .andExpect(jsonPath("accessToken").exists())
                .andExpect(jsonPath("refreshToken").exists())
                .andExpect(jsonPath("accessTokenExpireDate").value(3600000));
    }

    @Test
    @WithMockUser
    @DisplayName("PATCH /api/members/password")
    void updatePassword() throws Exception {
        //given
        Member member1 = createMember("email1@gmail.com");
        Member member2 = createMember("email2@gmail.com");
        Member member3 = createMember("email3@gmail.com");
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        TokenResponseDto token = memberService.login("email1@gmail.com", "123");
        PasswordRequestDto passwordRequestDto = new PasswordRequestDto("123", "123123");

        //when
        ResultActions result = mockMvc.perform(patch("/api/members/password")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-AUTH-TOKEN", token.getAccessToken())
                .content(objectMapper.writeValueAsString(passwordRequestDto))
        );

        //then
        result.andExpect(status().isCreated());
    }

    @Test
    @WithMockUser
    @DisplayName("PATCH /api/members/name")
    void updateName() throws Exception {
        //given
        Member member1 = createMember("email1@gmail.com");
        Member member2 = createMember("email2@gmail.com");
        Member member3 = createMember("email3@gmail.com");
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        TokenResponseDto token = memberService.login("email1@gmail.com", "123");

        //when
        ResultActions result = mockMvc.perform(patch("/api/members/name")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-AUTH-TOKEN", token.getAccessToken())
                .param("name", "update")
        );

        //then
        result.andExpect(status().isCreated());
    }

    @Test
    @WithMockUser
    @DisplayName("DELETE /api/members")
    void withdraw() throws Exception {
        //given
        Member member1 = createMember("email1@gmail.com");
        Member member2 = createMember("email2@gmail.com");
        Member member3 = createMember("email3@gmail.com");
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        TokenResponseDto token = memberService.login("email1@gmail.com", "123");

        //when
        ResultActions result = mockMvc.perform(delete("/api/members")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-AUTH-TOKEN", token.getAccessToken())
        );

        //then
        result.andExpect(status().isNoContent());
    }

    private Member createMember(String email) {
        return Member.builder()
                .email(email)
                .password(encoder.encode("123"))
                .name("tester")
                .build();
    }
}