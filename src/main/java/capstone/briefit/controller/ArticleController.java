package capstone.briefit.controller;

import capstone.briefit.apiPayload.ApiResponse;
import capstone.briefit.dto.ArticleResponseDTO;
import capstone.briefit.service.ArticleService;
import com.nimbusds.oauth2.sdk.ErrorResponse;
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
    public Object getArticles(@RequestHeader(value = "Authorization", required = false) String token, @RequestParam String category, @RequestParam String company) {
//        try {
//            if (category == null || company == null || page == null) {
//                return ApiResponse.error("400", "기사 목록 조회에 필요한 정보가 부족합니다.");
//            } else {
//                return ApiResponse.success("기사 목록 조회에 성공했습니다.", articleService.getArticles(token, category, company, page));
//            }
//        }catch (Exception e){
//            return ApiResponse.error("500", "기사 목록 조회에 실패했습니다.");
//        }
        return articleService.getArticles(token, category, company);
    }


    @GetMapping("/articles/recommend")
    public Object getRecommendedArticles(@RequestHeader(value = "Authorization") String token, @RequestParam String category, @RequestParam String company) {
//        try {
//            if (category == null || company == null || page == null) {
//                return ApiResponse.error("400", "추천 기사 목록 조회에 필요한 정보가 부족합니다.");
//            } else {
//                return ApiResponse.success("추천 기사 목록 조회에 성공했습니다.", articleService.recommendArticles(token, category, company, page));
//            }
//        }catch (Exception e){
//                return ApiResponse.error("500", "추천 기사 목록 조회에 실패했습니다.");
//        }
        return articleService.recommendArticles(token, category, company);
    }

    @GetMapping("/article")
    public ArticleResponseDTO.ArticleDetailInfoDTO getArticle(@RequestHeader(value = "Authorization", required = false) String token, @RequestParam("article-id") Long id) {
        return articleService.getArticle(token, id);
    }

    @GetMapping("/articles/search")
    public Object searchArticles(@RequestHeader(value = "Authorization", required = false) String token, @RequestParam("string") String string, @RequestParam String company) {
//        try {
//            if (string == null || company == null || page == null) {
//                return ApiResponse.error("400", "기사 검색 결과 목록 조회에 필요한 정보가 부족합니다.");
//            } else {
//                return ApiResponse.success("기사 검색 결과 목록 조회에 성공했습니다.", articleService.searchArticles(token, string, company, page));
//            }
//        }catch (Exception e){
//            return ApiResponse.error("500", "기사 검색 결과 목록 조회에 실패했습니다.");
//        }
        return articleService.searchArticles(token, string, company);
    }
}
