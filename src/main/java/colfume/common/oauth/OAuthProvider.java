package colfume.common.oauth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OAuthProvider {

    private String email;
    private String name;
    private String imageUrl;
    private String nameAttributeKey;
    private Map<String, Object> attributes;

    public static OAuthProvider of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        if (registrationId.equals("kakao")) {
            return ofKakao(userNameAttributeName, attributes);
        }
        if (registrationId.equals("google")) {
            return ofGoogle(userNameAttributeName, attributes);
        }
        return null;
    }

    public static OAuthProvider ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");

        return OAuthProvider.builder()
                .email((String) kakaoAccount.get("email"))
                .name((String) kakaoProfile.get("nickname"))
                .imageUrl((String) kakaoProfile.get("profile_img_url"))
                .nameAttributeKey(userNameAttributeName)
                .attributes(attributes)
                .build();
    }

    public static OAuthProvider ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthProvider.builder()
                .email((String) attributes.get("email"))
                .name((String) attributes.get("name"))
                .imageUrl((String) attributes.get("picture"))
                .nameAttributeKey(userNameAttributeName)
                .attributes(attributes)
                .build();
    }
}