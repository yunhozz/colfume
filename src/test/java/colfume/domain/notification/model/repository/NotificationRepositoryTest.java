package colfume.domain.notification.model.repository;

import colfume.domain.member.model.entity.Member;
import colfume.domain.member.model.repository.MemberRepository;
import colfume.domain.notification.model.Notification;
import colfume.domain.notification.dto.NotificationQueryDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class NotificationRepositoryTest {

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    MemberRepository memberRepository;

    @Test
    void findWithReceiverId() throws Exception {
        //given
        Member sender = createMember("sender@gmail.com", "sender");
        Member receiver = createMember("receiver@gmail.com", "receiver");
        memberRepository.save(sender);
        memberRepository.save(receiver);

        for (int i = 1; i <= 5; i++) {
            Notification notification = Notification.builder()
                    .sender(sender)
                    .receiver(receiver)
                    .message("test" + i)
                    .redirectUrl(null)
                    .build();
            notificationRepository.save(notification);
            Thread.sleep(10);
        }

        //when
        List<NotificationQueryDto> result = notificationRepository.findWithReceiverId(receiver.getId());

        //then
        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(5);
        assertThat(result).extracting("message").containsExactly("test5", "test4", "test3", "test2", "test1");
    }

    private Member createMember(String email, String name) {
        return Member.builder()
                .email(email)
                .password("123")
                .name(name)
                .build();
    }
}