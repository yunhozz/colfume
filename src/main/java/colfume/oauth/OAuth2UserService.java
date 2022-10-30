package colfume.oauth;

import colfume.domain.member.model.entity.Member;
import colfume.domain.member.model.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        Map<String, Object> attributes = delegate.loadUser(userRequest).getAttributes();

        OAuthProvider oAuthProvider = OAuthProvider.of(registrationId, userNameAttributeName, attributes);
        Member member = saveOrUpdate(oAuthProvider);
        List<String> roles = member.getMemberAuthorities().stream()
                .map(memberAuthority -> memberAuthority.getAuthority().getRole()).toList();

        return new UserPrincipal(member, member.getMemberAuthorities(), attributes);
    }

    private Member saveOrUpdate(OAuthProvider oAuthProvider) {
        Optional<Member> optionalMember = memberRepository.findByEmail(oAuthProvider.getEmail());
        if (optionalMember.isEmpty()) {
            Member member = Member.builder()
                    .email(oAuthProvider.getEmail())
                    .name(oAuthProvider.getName())
                    .imageUrl(oAuthProvider.getImageUrl())
                    .build();
            return memberRepository.save(member);
        }
        return optionalMember.get().update(oAuthProvider.getEmail(), oAuthProvider.getName(), oAuthProvider.getImageUrl());
    }
}