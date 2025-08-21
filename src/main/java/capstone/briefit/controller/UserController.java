package capstone.briefit.controller;

import capstone.briefit.domain.User;
import capstone.briefit.domain.enums.Tag;
import capstone.briefit.dto.CustomDTO;
import capstone.briefit.dto.UserResponseDTO;
import capstone.briefit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private static UserService userService;
//    private static SocialLoginService socialLoginService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
//        this.socialLoginService = socialLoginService;
    }

//    @PostMapping("/login/naver")
//    @ResponseBody
//    public ResponseEntity<String> naverLogin(@RequestParam(value = "code") String code, @RequestParam(value = "state") String state) throws UnsupportedEncodingException {
//        String jwtToken = socialLoginService.naverLogin(code, state);
//        return ResponseEntity.ok(jwtToken);
//    }

//    //테스트할 때 jwt 토큰을 얻어오는 용으로 잠시 사용할 API
//    @GetMapping("/login/naver/success")
//    public ResponseEntity<Map<String, String>> loginSuccess(@RequestParam String accessToken, @RequestParam String registration) {
//        Map<String, String> map = new HashMap<>();
//        map.put("accessToken", accessToken);
//        map.put("registration", registration);
//
//        // 토큰 검증 및 필요한 작업 수행
//        return ResponseEntity.ok(map);
//    }

    @PostMapping("/registration")
    public Boolean registration(@RequestHeader(value = "Authorization") String token, @RequestPart("userinfo") String userInfoJson, @RequestPart("profile") MultipartFile profile) {
        return userService.registration(token, userInfoJson, profile);
    }

    @GetMapping("")
    public UserResponseDTO.UserInfo getUserInfo(@RequestHeader(value = "Authorization") String token){
        return userService.getUserInfo(token);
    }

    @DeleteMapping("")
    public Boolean deleteUser(@RequestHeader(value = "Authorization") String token){
        return userService.deleteUser(token);
    }
}
