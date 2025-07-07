//package capstone.briefit.service;
//
//import capstone.briefit.config.JwtProvider;
//import capstone.briefit.domain.User;
//import capstone.briefit.domain.enums.Provider;
//import capstone.briefit.repository.UserRepository;
//import com.fasterxml.jackson.databind.JsonNode;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.*;
//import org.springframework.stereotype.Service;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.client.RestTemplate;
//
//import java.io.UnsupportedEncodingException;
//import java.net.URLEncoder;
//import java.nio.charset.StandardCharsets;
//import java.util.Optional;
//
//@Service
//public class SocialLoginService {
//
//    private final UserRepository userRepository;
//    private final JwtProvider jwtTokenProvider;
//    private final RestTemplate restTemplate;
//
//    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
//    private String naverClientId;
//
//    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
//    private String naverClientSecret;
//
//    @Value("${spring.security.oauth2.client.registration.naver.redirect-uri}")
//    private String naverRedirectUri;
//
//    @Value("${spring.security.oauth2.client.provider.naver.token-uri}")
//    private String naverTokenUri;
//
//    @Value("${spring.security.oauth2.client.provider.naver.user-info-uri}")
//    private String naverUserInfoUri;
//
//    @Autowired
//    public SocialLoginService(UserRepository userRepository, JwtProvider jwtProvider, RestTemplate restTemplate) {
//        this.userRepository = userRepository;
//        this.jwtTokenProvider = jwtProvider;
//        this.restTemplate = restTemplate;
//    }
//
//    public String naverLogin(String authorizationCode, String state) throws UnsupportedEncodingException {
//        // 1. 인가 코드로 Access Token 요청
//        String accessToken = getNaverAccessToken(authorizationCode, state);
//        if (accessToken == null) {
//            throw new RuntimeException("Failed to get Google Access Token");
//        }
//
//        // 2. Access Token으로 사용자 정보 요청
//        JsonNode userProfile = getNaverUserProfile(accessToken);
//        if (userProfile == null) {
//            throw new RuntimeException("Failed to get Google User Profile");
//        }
//
//        String providerId = userProfile.get("response").get("id").asText(); // Google의 고유 ID
//
//        // 3. 사용자 정보로 회원가입 또는 로그인 처리
//        User user = findOrCreateUser(Provider.naver, providerId);
//
//        // 4. JWT 토큰 생성
//        String jwtToken = jwtTokenProvider.generateToken(user);
//
//        return jwtToken;
//    }
//
//    private String getNaverAccessToken(String authorizationCode, String state) throws UnsupportedEncodingException {
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        params.add("code", authorizationCode);
//        params.add("client_id", naverClientId);
//        params.add("client_secret", naverClientSecret);
////        params.add("redirect_uri", naverRedirectUri);
//        params.add("grant_type", "authorization_code");
//        params.add("state", URLEncoder.encode(state, StandardCharsets.UTF_8.toString()));
//
//        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
//
//        ResponseEntity<JsonNode> response = restTemplate.postForEntity(naverTokenUri, request, JsonNode.class);
//
//        System.out.println("Naver Token Response Body: " + response.getBody());
//
//        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
//            return response.getBody().get("access_token").asText();
//        }
//        return null;
//    }
//
//    private JsonNode getNaverUserProfile(String accessToken) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.setBearerAuth(accessToken);
//        HttpEntity<String> request = new HttpEntity<>(headers);
//
//        ResponseEntity<JsonNode> response = restTemplate.exchange(naverUserInfoUri, HttpMethod.GET, request, JsonNode.class);
//
//        if (response.getStatusCode().is2xxSuccessful()) {
//            return response.getBody();
//        }
//        return null;
//    }
//
//    private User findOrCreateUser(Provider provider, String providedId) {
//        Optional<User> user = userRepository.findByProvidedId(providedId);
//
//        if (user.isPresent()) {
//            return user.get();
//        } else {
//            User newUser = User.builder()
//                    .provider(provider)
//                    .providedId(providedId)
//                    .build();
//            return userRepository.save(newUser);
//        }
//    }
//}
