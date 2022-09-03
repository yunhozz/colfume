package colfume.domain.member.model.repository;

import colfume.domain.member.model.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void findByEmail() throws Exception {
        //given
        Member member1 = createMember("email1@gmail.com");
        Member member2 = createMember("email2@gmail.com");
        Member member3 = createMember("email3@gmail.com");
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        //when
        Member result = memberRepository.findByEmail("email1@gmail.com").get();

        //then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(member1);
    }

    private Member createMember(String email) {
        return Member.builder()
                .email(email)
                .password("123")
                .name("tester")
                .build();
    }
}