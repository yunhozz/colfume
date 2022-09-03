package colfume.api;

import colfume.domain.member.model.entity.Member;
import colfume.domain.member.model.repository.MemberRepository;
import colfume.domain.member.service.MemberService;
import colfume.dto.MemberDto;
import colfume.util.JwtProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @BeforeEach
    public void beforeEach() {
        Member member1 = createMember("email1@gmail.com");
        Member member2 = createMember("email2@gmail.com");
        Member member3 = createMember("email3@gmail.com");
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
    }

    @Test
    @WithMockUser
    @DisplayName("GET /api/members/{memberId}")
    void getMember() throws Exception {
        //given
        Member member = memberRepository.getReferenceById(1L);

        //when
        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.get("/api/members/{memberId}", member.getId())
                .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1L))
                .andExpect(jsonPath("email").value("email1@gmail.com"))
                .andExpect(jsonPath("password").exists())
                .andExpect(jsonPath("name").value("tester"));
    }

    @Test
    @WithMockUser
    @DisplayName("GET /api/members")
    void getMembers() throws Exception {
        //when
        ResultActions result = mockMvc.perform(get("/api/members")
                .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].email").value("email1@gmail.com"))
                .andExpect(jsonPath("$[0].password").exists())
                .andExpect(jsonPath("$[0].name").value("tester"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].email").value("email2@gmail.com"))
                .andExpect(jsonPath("$[1].password").exists())
                .andExpect(jsonPath("$[1].name").value("tester"))
                .andExpect(jsonPath("$[2].id").value(3L))
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
        result.andExpect(status().isOk())
                .andExpect(content().string("4"));
    }

    @Test
    @WithMockUser
    @DisplayName("POST /api/members/login")
    void login() throws Exception {
        //given
        LoginRequestDto loginRequestDto = new LoginRequestDto("email1@gmail.com", "123");

        //when
        ResultActions result = mockMvc.perform(post("/api/members/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequestDto))
        );

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("grantType").value("Bearer"))
                .andExpect(jsonPath("accessToken").exists())
                .andExpect(jsonPath("refreshToken").exists())
                .andExpect(jsonPath("accessTokenExpireDate").value(3600000));
    }

    private Member createMember(String email) {
        return Member.builder()
                .email(email)
                .password(encoder.encode("123"))
                .name("tester")
                .build();
    }
}