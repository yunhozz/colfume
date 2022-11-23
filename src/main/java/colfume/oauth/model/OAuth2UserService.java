package colfume.oauth.model;

import colfume.common.enums.Role;
import colfume.domain.member.model.entity.Member;
import colfume.domain.member.model.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Slf4j
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

        OAuth2Provider oAuth2Provider = OAuth2Provider.of(registrationId, userNameAttributeName, attributes);
        Member member = saveOrUpdate(oAuth2Provider);
        log.info("email = " + attributes.get("email"));
        log.info("name = " + attributes.get("name"));

        return new UserPrincipal(member, attributes);
    }

    private Member saveOrUpdate(OAuth2Provider oAuth2Provider) {
        final Member[] member = {null};
        memberRepository.findByEmail(oAuth2Provider.getEmail())
                .ifPresentOrElse(m -> m.update(oAuth2Provider.getEmail(), oAuth2Provider.getName(), oAuth2Provider.getImageUrl()), () -> {
                    member[0] = Member.builder()
                            .email(oAuth2Provider.getEmail())
                            .name(oAuth2Provider.getName())
                            .imageUrl(oAuth2Provider.getImageUrl())
                            .role(Role.USER)
                            .build();
                    memberRepository.save(member[0]);
                });

        return member[0];
    }
}