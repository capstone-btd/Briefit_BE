package capstone.briefit.controller;

import capstone.briefit.dto.ArticleResponseDTO;
import capstone.briefit.dto.CustomDTO;
import capstone.briefit.dto.ScrapResponseDTO;
import capstone.briefit.service.ScrapService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class ScrapController {
    private static ScrapService scrapService;

    @Autowired
    public ScrapController(ScrapService scrapService) {
        this.scrapService = scrapService;
    }

    @PostMapping("/scrap")
    public Long scrapArticle(@RequestHeader(value = "Authorization") String token, @RequestParam("article-id") Long id){
        return scrapService.scrapAticle(token, id);
    }

    @GetMapping("/scraps")
    public List<ArticleResponseDTO.ArticleInfoDTO> getScraps(@RequestHeader(value = "Authorization") String token, @RequestParam String category){
        return scrapService.getScraps(token, category);
    }

//    @GetMapping("/scrap")
//    public ArticleResponseDTO.ArticleDetailInfoDTO getScrap(@RequestHeader(value = "Authorization") String token, @RequestParam("scrap-id") Long id){
//        return scrapService.getScrap(token, id);
//    }

    @DeleteMapping("/scraps")
    public Boolean deleteScraps(@RequestBody List<Long> scrapIds){
        return scrapService.deleteScraps(scrapIds);
    }

}
