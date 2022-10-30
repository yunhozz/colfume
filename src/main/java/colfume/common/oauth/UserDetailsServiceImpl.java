package colfume.common.oauth;

import colfume.common.enums.ErrorCode;
import colfume.domain.member.model.entity.Member;
import colfume.domain.member.model.entity.MemberAuthority;
import colfume.domain.member.model.repository.MemberAuthorityRepository;
import colfume.domain.member.model.repository.MemberRepository;
import colfume.domain.member.service.exception.EmailNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final MemberAuthorityRepository memberAuthorityRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new EmailNotFoundException(ErrorCode.EMAIL_NOT_FOUND));
        Set<MemberAuthority> memberAuthorities = memberAuthorityRepository.findByMember(member);

        return new UserPrincipal(member, memberAuthorities);
    }
}