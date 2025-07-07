package capstone.briefit.controller;

import capstone.briefit.dto.ArticleResponseDTO;
import capstone.briefit.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ArticleController {
    private static ArticleService articleService;

    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/articles")
    public List<ArticleResponseDTO.ArticleInfoDTO> getArticles(@RequestHeader(value = "Authorization", required = false) String token, @RequestParam String category){
        return articleService.getArticles(token, category);
    }

    @GetMapping("/articles/recommend")
    public List<ArticleResponseDTO.ArticleInfoDTO> getRecommendedArticles(@RequestHeader(value = "Authorization") String token, @RequestParam String category){
        return articleService.recommendArticles(token, category);
    }

    @GetMapping("/article")
    public ArticleResponseDTO.ArticleDetailInfoDTO getArticle(@RequestParam("article-id") Long id) {
        return articleService.getArticle(id);
    }

    @GetMapping("/articles/search")
    public List<ArticleResponseDTO.ArticleInfoDTO> searchArticles(@RequestHeader(value = "Authorization", required = false) String token, @RequestParam("string") String string) {
        return articleService.searchArticles(token, string);
    }

}
