package colfume.domain.member.service;

import colfume.domain.member.model.entity.Member;
import colfume.domain.member.model.repository.MemberRepository;
import colfume.enums.ErrorCode;
import colfume.exception.EmailNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new EmailNotFoundException(ErrorCode.EMAIL_NOT_FOUND));

        return new UserDetailsImpl(member);
    }
}
