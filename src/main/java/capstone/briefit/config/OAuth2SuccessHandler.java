package capstone.briefit.security;

import capstone.briefit.config.JwtProvider;
import capstone.briefit.domain.User;
import capstone.briefit.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtProvider jwtProvider;

    @Autowired
    public OAuth2SuccessHandler(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        // 유저 정보에서 JWT 생성 (예: 이메일 기반)
        String jwtToken = (String) oAuth2User.getAttributes().get("jwtToken");
        String registration = (String) oAuth2User.getAttributes().get("registration");

        // 토큰을 URL 쿼리 파라미터로 포함하여 프론트엔드 로그인 성공 페이지로 리다이렉트
        String targetUrl = "http://localhost:3000/users/login/naver/success?accessToken=" + jwtToken + "&registration=" + registration;

        // 리다이렉트
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
//        // 유저 정보에서 JWT 토큰과 등록 여부를 가져옴
//        String jwtToken = (String) oAuth2User.getAttributes().get("jwtToken");
//        String registration = (String) oAuth2User.getAttributes().get("registration");
//
//        // JWT 토큰과 등록 여부을 JSON 형식으로 반환
//        response.setStatus(HttpServletResponse.SC_OK);
//        response.setContentType("application/json");
//        response.getWriter().write("{"
//                + "\"accessToken\": \"" + jwtToken + "\","
//                + "\"registration\": \"" + registration + "\""
//                + "}");
//    }
}