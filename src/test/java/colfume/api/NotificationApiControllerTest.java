package colfume.api;

import colfume.common.dto.TokenResponseDto;
import colfume.domain.member.model.entity.Member;
import colfume.domain.member.model.repository.MemberRepository;
import colfume.domain.member.service.AuthService;
import colfume.domain.notification.dto.NotificationRequestDto;
import colfume.domain.notification.service.NotificationService;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class NotificationApiControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    NotificationService notificationService;

    @Autowired
    AuthService authService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    BCryptPasswordEncoder encoder;

    @Test
    @WithMockUser
    @DisplayName("GET /api/notifications/{notificationId}")
    void getNotification() throws Exception {
        //given
        Member sender = createMember("sender@gmail.com", "sender");
        Member receiver = createMember("receiver@gmail.com", "receiver");
        memberRepository.save(sender);
        memberRepository.save(receiver);

        NotificationRequestDto notificationRequestDto = new NotificationRequestDto("test", null);
        Long notificationId = notificationService.sendNotification(notificationRequestDto, sender.getId(), receiver.getId());

        //when
        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.get("/api/notifications/{notificationId}", String.valueOf(notificationId))
                .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("id").value(notificationId))
                .andExpect(jsonPath("senderId").value(sender.getId()))
                .andExpect(jsonPath("receiverId").value(receiver.getId()))
                .andExpect(jsonPath("message").value("test"))
                .andExpect(jsonPath("isChecked").value(true));
    }

    @Test
    @WithMockUser
    @DisplayName("GET /api/notifications")
    void getNotifications() throws Exception {
        //given
        Member sender = createMember("sender@gmail.com", "sender");
        Member receiver = createMember("receiver@gmail.com", "receiver");
        memberRepository.save(sender);
        memberRepository.save(receiver);

        for (int i = 1; i <= 3; i++) {
            NotificationRequestDto notificationRequestDto = new NotificationRequestDto("test" + i, null);
            notificationService.sendNotification(notificationRequestDto, sender.getId(), receiver.getId());
            Thread.sleep(10);
        }

        TokenResponseDto token = authService.login("receiver@gmail.com", "123");

        //when
        ResultActions result = mockMvc.perform(get("/api/notifications")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-AUTH-TOKEN", token.getAccessToken())
        );

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].message").value("test3"))
                .andExpect(jsonPath("$[0].isChecked").value(false))
                .andExpect(jsonPath("$[0].senderId").value(sender.getId()))
                .andExpect(jsonPath("$[0].receiverId").value(receiver.getId()))
                .andExpect(jsonPath("$[0].name").value(sender.getName()))
                .andExpect(jsonPath("$[1].id").exists())
                .andExpect(jsonPath("$[1].message").value("test2"))
                .andExpect(jsonPath("$[2].id").exists())
                .andExpect(jsonPath("$[2].message").value("test1"));
    }

    @Test
    @WithMockUser
    @DisplayName("POST /api/notifications")
    void createNotification() throws Exception {
        //given
        Member sender = createMember("sender@gmail.com", "sender");
        Member receiver = createMember("receiver@gmail.com", "receiver");
        memberRepository.save(sender);
        memberRepository.save(receiver);

        NotificationRequestDto notificationRequestDto = new NotificationRequestDto("test", null);
        TokenResponseDto token = authService.login("sender@gmail.com", "123");

        //when
        ResultActions result = mockMvc.perform(post("/api/notifications")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-AUTH-TOKEN", token.getAccessToken())
                .content(objectMapper.writeValueAsString(notificationRequestDto))
                .param("receiverId", String.valueOf(receiver.getId()))
        );

        //then
        result.andExpect(status().isCreated());
    }

    @Test
    @WithMockUser
    @DisplayName("DELETE /api/notifications")
    void deleteNotification() throws Exception {
        //given
        Member sender = createMember("sender@gmail.com", "sender");
        Member receiver = createMember("receiver@gmail.com", "receiver");
        memberRepository.save(sender);
        memberRepository.save(receiver);

        NotificationRequestDto notificationRequestDto = new NotificationRequestDto("test", null);
        Long notificationId = notificationService.sendNotification(notificationRequestDto, sender.getId(), receiver.getId());

        //when
        ResultActions result = mockMvc.perform(delete("/api/notifications")
                .contentType(MediaType.APPLICATION_JSON)
                .param("notificationId", String.valueOf(notificationId))
        );

        //then
        result.andExpect(status().isNoContent());
    }

    private Member createMember(String email, String name) {
        return Member.builder()
                .email(email)
                .password(encoder.encode("123"))
                .name(name)
                .build();
    }
}