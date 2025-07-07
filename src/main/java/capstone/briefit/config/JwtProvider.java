package capstone.briefit.config;

import capstone.briefit.domain.User;
import capstone.briefit.repository.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtProvider {
    private Key key;
    private final long validityInMilliseconds = 3600000; // 1시간
    private final UserRepository userRepository;

    @Autowired
    public JwtProvider(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void init() {
        key = Keys.secretKeyFor(SignatureAlgorithm.HS256); // 자동으로 안전한 키 생성. 껐다 켜지면 키값 변경돼서 꺼지기 전에 발급 됐던 토큰들은 사용 못 함.
    }

    // 사용자 정보(예: 이메일, 권한)를 담아 JWT 토큰 생성.
    public String generateToken(User user) {
        Claims claims = Jwts.claims().setSubject(user.getProvidedId());
        claims.put("role", "ROLE_USER");

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // JWT에서 사용자 식별 정보와 권한을 꺼내 Spring Security 인증 객체로 변환
    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);
        String providedId = claims.getSubject();

        @SuppressWarnings("unchecked")
        String role = (String) claims.get("role");

        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);

        // 여기서는 providedId을 principal로, 권한을 포함하는 Authentication 생성
        return new UsernamePasswordAuthenticationToken(providedId, "", Collections.singletonList(authority));
    }

    // 토큰이 변조되었거나 만료됐는지 체크.
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            return !claimsJws.getBody().getExpiration().before(new Date());

        } catch (JwtException | IllegalArgumentException e) {
            // 예외 발생 시 토큰 무효 처리
            return false;
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public User getUserByToken(String token) {
        return userRepository.findByProvidedId(parseClaims(token.substring(7)).getSubject()).get(); // subject에 ProvidedId 넣었음. 따라서 결과로 ProvidedId를 얻음
    }
}
