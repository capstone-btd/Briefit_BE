package capstone.briefit.service;

import capstone.briefit.aws.AmazonS3Manager;
import capstone.briefit.config.JwtProvider;
import capstone.briefit.domain.User;
import capstone.briefit.domain.UserTag;
import capstone.briefit.domain.Uuid;
import capstone.briefit.domain.Wordcloud;
import capstone.briefit.domain.enums.Tag;
import capstone.briefit.dto.UserResponseDTO;
import capstone.briefit.repository.UserRepository;
import capstone.briefit.repository.UserTagRepository;
import capstone.briefit.repository.UuidRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@Transactional
public class UserService {
    private final UserTagRepository userTagRepository;
    private final JwtProvider jwtProvider;
    private final AmazonS3Manager amazonS3Manager;
    private final UuidRepository uuidRepository;

    @Autowired
    public UserService(UserTagRepository userTagRepository, JwtProvider jwtProvider, AmazonS3Manager amazonS3Manager, UuidRepository uuidRepository) {
        this.userTagRepository = userTagRepository;
        this.jwtProvider = jwtProvider;
        this.amazonS3Manager = amazonS3Manager;
        this.uuidRepository = uuidRepository;
    }

    public Boolean registration(String token, String userInfoJson, MultipartFile profile) {
        ObjectMapper objectMapper = new ObjectMapper();
        String nickname = null;
        List<Tag> tags = new ArrayList<>();
        try {
            JsonNode root = objectMapper.readTree(userInfoJson);
            JsonNode categories = root.path("categories");
            nickname = root.path("nickname").asText();

            for (JsonNode category : categories) {
                tags.add(Tag.valueOf(category.asText()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Uuid uuid = uuidRepository.save(Uuid.builder().uuid(UUID.randomUUID().toString()).build());
        String profileUrl = amazonS3Manager.uploadFile(amazonS3Manager.generateProfileKeyName(uuid), profile);

        User user = jwtProvider.getUserByToken(token);
        user.setNickname(nickname);
        user.setProfileUrl(profileUrl);

        if(!user.getUserTags().isEmpty()){
            userTagRepository.deleteAllByUser(user);
        }
        for(Tag tag : tags){
            userTagRepository.save(UserTag.builder().tag(tag).user(user).build());
        }

        return Boolean.TRUE;
    }

    public UserResponseDTO.UserInfo getUserInfo(String token){
        User user = jwtProvider.getUserByToken(token);

        List<Tag> tags = new ArrayList<>();
        for(UserTag userTag : user.getUserTags()){
            tags.add(userTag.getTag());
        }

        return UserResponseDTO.UserInfo.builder().nickname(user.getNickname()).profileUrl(user.getProfileUrl()).categories(tags).build();
    }

    public Boolean updateUserInfo(String token, User user){
        User updateUser = jwtProvider.getUserByToken(token);
        updateUser.setNickname(user.getNickname());
        updateUser.setUserTags(user.getUserTags());
        return Boolean.TRUE;
    }
}
