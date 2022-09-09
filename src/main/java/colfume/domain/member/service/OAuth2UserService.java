package colfume.domain.member.service;

import colfume.domain.member.model.entity.Member;
import colfume.domain.member.model.repository.MemberRepository;
import colfume.dto.OAuthDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        Map<String, Object> attributes = delegate.loadUser(userRequest).getAttributes();

        OAuthDto oAuthDto = OAuthDto.of(registrationId, userNameAttributeName, attributes);
        Member member = saveOrUpdate(oAuthDto);
        List<String> roles = member.getMemberAuthorities().stream()
                .map(memberAuthority -> memberAuthority.getAuthority().getRole()).toList();
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        roles.forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));

        return new DefaultOAuth2User(authorities, oAuthDto.getAttributes(), oAuthDto.getNameAttributeKey());
    }

    private Member saveOrUpdate(OAuthDto oAuthDto) {
        Optional<Member> optionalMember = memberRepository.findByEmail(oAuthDto.getEmail());
        if (optionalMember.isEmpty()) {
            Member member = Member.builder()
                    .email(oAuthDto.getEmail())
                    .name(oAuthDto.getName())
                    .imageUrl(oAuthDto.getImageUrl())
                    .build();
            return memberRepository.save(member);
        }
        return optionalMember.get().update(oAuthDto.getEmail(), oAuthDto.getName(), oAuthDto.getImageUrl());
    }
}