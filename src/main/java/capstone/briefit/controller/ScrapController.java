package capstone.briefit.controller;

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
    public List<ScrapResponseDTO.ScrapInfoDTO> getScraps(@RequestHeader(value = "Authorization") String token, @RequestParam String category){
        return scrapService.getScraps(token, category);
    }

    @GetMapping("/scrap")
    public ScrapResponseDTO.ScrapDetailInfoDTO getScrap(@RequestHeader(value = "Authorization") String token, @RequestParam("scrap-id") Long id){
        return scrapService.getScrap(id);
    }

    @PostMapping("/scrap/customize")
    public Boolean customizeScrap(@RequestHeader(value = "Authorization") String token, @RequestParam("scrap-id") Long id, @RequestBody CustomDTO.CustomizeRequestInfoDTO customizeRequestInfoDTO){
        return scrapService.customizeScrap(id, customizeRequestInfoDTO);
    }

    @GetMapping("/scrap/customize")
    public List<ScrapResponseDTO.ScrapInfoDTO> getCustomizeArticle(@RequestHeader(value = "Authorization") String token, @RequestParam String category){
        return scrapService.getCustomizeArticles(token, category);
    }

    @DeleteMapping("/scrap")
    public Boolean deleteScrap(@RequestBody List<Long> scrapIds){
        return scrapService.deleteScrap(scrapIds);
    }

    @DeleteMapping("/scrap/customize")
    public Boolean deleteCustomize(@RequestBody List<Long> scrapIds){
        return scrapService.deleteCustomize(scrapIds);
    }

}
