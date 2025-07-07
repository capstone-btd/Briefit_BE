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

//    //프론트랑 연동하기 전에 jwt 토큰 얻어오기 용으로 잠시 사용할 API
//    @GetMapping("/login/naver/success")
//    public ResponseEntity<Map<String, String>> loginSuccess(@RequestParam("jwt-token") String token) {
//        UserResponseDTO.UserInfo user = userService.getUserInfo("Bearer " + token);
//
//        Map<String, String> map = new HashMap<>();
//        map.put("jwt-token", token);
//
//        if(user.nickname() == null){
//            map.put("state", "new");
//        }else{
//            map.put("state", "old");
//        }
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

}
