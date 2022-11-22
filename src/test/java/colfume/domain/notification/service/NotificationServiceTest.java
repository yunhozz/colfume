package colfume.domain.notification.service;

import colfume.domain.notification.dto.request.NotificationRequestDto;
import colfume.domain.member.model.entity.Member;
import colfume.domain.member.model.repository.MemberRepository;
import colfume.domain.notification.model.entity.Notification;
import colfume.domain.notification.model.repository.NotificationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class NotificationServiceTest {

    @Autowired
    NotificationService notificationService;

    @Autowired
    ConnectService connectService;

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    MemberRepository memberRepository;

    @Test
    void connect() throws Exception {
        //given
        Member member = createMember("sender@gmail.com", "sender");
        memberRepository.save(member);

        //when
        SseEmitter emitter = connectService.connect(member.getId(), "");

        //then
        assertThat(emitter).isNotNull();
        assertThat(emitter.getTimeout()).isEqualTo(60 * 60 * 1000L);
    }

    @Test
    void sendNotification() throws Exception {
        //given
        Member sender = createMember("sender@gmail.com", "sender");
        Member receiver = createMember("receiver@gmail.com", "receiver");
        memberRepository.save(sender);
        memberRepository.save(receiver);

        NotificationRequestDto notificationRequestDto = new NotificationRequestDto("test", null);

        //when
        Long notificationId = notificationService.sendNotification(notificationRequestDto, sender.getId(), receiver.getId());
        Optional<Notification> result = notificationRepository.findById(notificationId);

        //then
        assertThat(result).isNotNull();
        assertThat(result.get().getMessage()).isEqualTo("test");
    }

    @Test
    void readNotification() throws Exception {
        //given
        Member sender = createMember("sender@gmail.com", "sender");
        Member receiver = createMember("receiver@gmail.com", "receiver");
        memberRepository.save(sender);
        memberRepository.save(receiver);

        Notification notification = Notification.builder()
                .sender(sender)
                .receiver(receiver)
                .message("test")
                .redirectUrl(null)
                .build();
        notificationRepository.save(notification);

        //when
        notificationService.readNotification(notification.getId());

        //then
        assertThat(notification).isNotNull();
        assertThat(notification.isChecked()).isTrue();
    }

    @Test
    void deleteNotification() throws Exception {
        //given
        Member sender = createMember("sender@gmail.com", "sender");
        Member receiver = createMember("receiver@gmail.com", "receiver");
        memberRepository.save(sender);
        memberRepository.save(receiver);

        Notification notification = Notification.builder()
                .sender(sender)
                .receiver(receiver)
                .message("test")
                .redirectUrl(null)
                .build();
        notificationRepository.save(notification);

        //when
        notificationService.deleteNotification(notification.getId());
        List<Notification> result = notificationRepository.findAll();

        //then
        assertThat(result.size()).isEqualTo(0);
    }

    private Member createMember(String email, String name) {
        return Member.builder()
                .email(email)
                .password("123")
                .name(name)
                .build();
    }
}