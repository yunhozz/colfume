package colfume.domain.member.service;

import colfume.domain.member.model.entity.Member;
import colfume.domain.member.model.entity.MemberAuthority;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Getter
public class UserDetailsImpl implements UserDetails {

    private final Long id;
    private final String email;
    private final String password;
    private final String name;
    private final Set<MemberAuthority> memberAuthorities;

    public UserDetailsImpl(Member member) {
        id = member.getId();
        email = member.getEmail();
        password = member.getPassword();
        name = member.getName();
        memberAuthorities = member.getMemberAuthorities();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<String> roles = memberAuthorities.stream()
                .map(memberAuthority -> memberAuthority.getAuthority().getRole()).toList();
        Set<GrantedAuthority> authorities = new HashSet<>();
        roles.forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));

        return authorities;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
