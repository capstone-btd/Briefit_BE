package capstone.briefit.service;

import capstone.briefit.config.JwtProvider;
import capstone.briefit.domain.User;
import capstone.briefit.domain.enums.Provider;
import capstone.briefit.domain.enums.Status;
import capstone.briefit.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private UserRepository userRepository;
    private JwtProvider jwtProvider;

    @Autowired
    public CustomOAuth2UserService(UserRepository userRepository, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 네이버는 response 안에 유저정보가 들어있음
        Map<String, Object> attributes = oAuth2User.getAttribute("response");
//        String email = (String) attributes.get("email");
        String naverId = (String) attributes.get("id");

        // DB에서 유저 조회/저장
        User user = userRepository.findByProvidedId(naverId)
                .orElseGet(() -> userRepository.save(User
                        .builder()
                        .providedId(naverId)
//                        .email(email)
                        .provider(Provider.naver)
                        .status(Status.ACTIVE)
                        .build()));

        // JWT 발급
        String jwtToken = jwtProvider.generateToken(user);

        // attributes에 jwtToken 추가해서 반환(프론트에 JWT 내려줄 때 활용)
        Map<String, Object> customAttributes = new HashMap<>(attributes);
        customAttributes.put("jwtToken", jwtToken);

        if(user.getNickname() == null){
            customAttributes.put("registration", "no");
        }else{
            customAttributes.put("registration", "yes");
        }

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                customAttributes, "id"
        );
    }
}
