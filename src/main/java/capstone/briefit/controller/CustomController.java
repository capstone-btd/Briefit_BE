package capstone.briefit.controller;

import capstone.briefit.dto.ArticleResponseDTO;
import capstone.briefit.dto.CustomDTO;
import capstone.briefit.dto.ScrapResponseDTO;
import capstone.briefit.service.CustomService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class CustomController {
    private static CustomService customService;

    @Autowired
    public CustomController(CustomService customService) {
        this.customService = customService;
    }

    @PostMapping("/custom")
    public Long customizeArticle(@RequestHeader(value = "Authorization") String token, @RequestParam("article-id") Long id, @RequestBody CustomDTO.CustomizeRequestDTO customizeRequestInfoDTO){
        return customService.customizeArticle(token, id, customizeRequestInfoDTO);
    }

    @GetMapping("/customs")
    public List<ArticleResponseDTO.ArticleInfoDTO> getCustomArticles(@RequestHeader(value = "Authorization") String token, @RequestParam String category){
        return customService.getCustomArticles(token, category);
    }

    @DeleteMapping("/customs")
    public Boolean deleteCustomArticles(@RequestBody List<Long> customIds){
        return customService.deleteCustomArticles(customIds);
    }
}
