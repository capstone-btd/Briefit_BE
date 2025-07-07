package capstone.briefit.service;

import capstone.briefit.config.JwtProvider;
import capstone.briefit.domain.*;
import capstone.briefit.domain.enums.Category;
import capstone.briefit.domain.enums.Company;
import capstone.briefit.dto.CustomDTO;
import capstone.briefit.dto.ScrapResponseDTO;
import capstone.briefit.dto.SourceDTO;
import capstone.briefit.repository.ArticleRepository;
import capstone.briefit.repository.CustomRepository;
import capstone.briefit.repository.ScrapRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ScrapService {
    private final ScrapRepository scrapRepository;
    private final JwtProvider jwtProvider;
    private final ArticleRepository articleRepository;
    private final CustomRepository customRepository;

    @Autowired
    public ScrapService(ScrapRepository scrapRepository, JwtProvider jwtProvider, ArticleRepository articleRepository, CustomRepository customRepository) {
        this.scrapRepository = scrapRepository;
        this.jwtProvider = jwtProvider;
        this.articleRepository = articleRepository;
        this.customRepository = customRepository;
    }

    public Long scrapAticle(String token, Long articleId){
        User user = jwtProvider.getUserByToken(token);
        Article article = articleRepository.findById(articleId).get();
        UserScrap userScrap = scrapRepository.save(UserScrap.builder().user(user).article(article).backgroundColor("white-theme").fontColor("black").fontSize(10).build());
        return userScrap.getId();
    }

    public List<ScrapResponseDTO.ScrapInfoDTO> getScraps(String token, String category){
        User user = jwtProvider.getUserByToken(token);

        List<ScrapResponseDTO.ScrapInfoDTO> userScraps = new ArrayList<>();
        for(UserScrap scrap : user.getUserScraps()){
            List<Category> categories = new ArrayList<>();
            for(ArticleCategory articleCategory : scrap.getArticle().getArticleCategories()){
                categories.add(articleCategory.getCategory());
            }

            if(!category.equals("전체") && !categories.contains(Category.valueOf(category))){
                continue;
            }

            List<Company> companies = new ArrayList<>();
            for(ArticleSource source : scrap.getArticle().getArticleSources()){
                companies.add(source.getPressCompany());
            }

            List<String> imgUrls = new ArrayList<>();
            for(ArticleImage articleImage: scrap.getArticle().getArticleImages()){
                imgUrls.add(articleImage.getImageUrl());
            }

            Boolean isCustomize = true;
            if(scrap.getBackgroundColor().equals("white-theme") && scrap.getScrapCustoms().isEmpty()) {
                isCustomize = false;
            }

            userScraps.add(ScrapResponseDTO.ScrapInfoDTO
                    .builder()
                    .scrapId(scrap.getId())
                    .isCustomize(isCustomize)
                    .title(scrap.getArticle().getTitle())
                    .body(scrap.getArticle().getBody())
                    .categories(categories)
                    .pressCompanies(companies)
                    .imgUrls(imgUrls)
                    .backgroundColor(scrap.getBackgroundColor())
                    .createdAt(scrap.getCreatedAt())
//                    .backgroundColor(scrap.getBackgroundColor())
//                    .fontColor(scrap.getFontColor())
//                    .fontSize(scrap.getFontSize())
                    .build());
        }
        return userScraps;
    }

    public ScrapResponseDTO.ScrapDetailInfoDTO getScrap(Long scrapId){
        UserScrap scrap = scrapRepository.findById(scrapId).get();

        List<Category> categories = new ArrayList<>();
        for(ArticleCategory articleCategory : scrap.getArticle().getArticleCategories()){
            categories.add(articleCategory.getCategory());
        }

        List<SourceDTO.SourceInfoDTO> sources = new ArrayList<>();
        for(ArticleSource source : scrap.getArticle().getArticleSources()) {
            sources.add(SourceDTO.SourceInfoDTO
                    .builder()
                    .url(source.getUrl())
                    .pressCompany(source.getPressCompany())
                    .sourceTitle(source.getTitle())
                    .build());
        }

        List<CustomDTO.HighlightsInfoDTO> highlightsInfos = new ArrayList<>();
        for(ScrapCustom custom : scrap.getScrapCustoms()){
            highlightsInfos.add(CustomDTO.HighlightsInfoDTO
                    .builder()
                    .startPoint(custom.getStartPoint())
                    .endPoint(custom.getEndPoint())
                    .highlightsColor(custom.getHighlightsColor())
                    .highlightsFontColor(custom.getFontColor())
                    .highlightsFontSize(custom.getFontSize())
                    .isBold(custom.getIsBold())
                    .build());
        }

        List<String> imgUrls = new ArrayList<>();
        for(ArticleImage articleImage: scrap.getArticle().getArticleImages()){
            imgUrls.add(articleImage.getImageUrl());
        }

        return ScrapResponseDTO.ScrapDetailInfoDTO
                .builder()
                .scrapId(scrap.getId())
                .title(scrap.getArticle().getTitle())
                .body(scrap.getArticle().getBody())
                .categories(categories)
                .sources(sources)
                .imgUrls(imgUrls)
                .backgroundColor(scrap.getBackgroundColor())
                .fontColor(scrap.getFontColor())
                .fontSize(scrap.getFontSize())
                .customs(highlightsInfos)
                .createdAt(scrap.getCreatedAt())
                .build();
    }

    public Boolean customizeScrap(Long id, CustomDTO.CustomizeRequestInfoDTO customizeRequestInfoDTO) {
        UserScrap userScrap = scrapRepository.findById(id).get();

        if(!customizeRequestInfoDTO.backgroundColor().equals(userScrap.getBackgroundColor())){
            userScrap.setBackgroundColor(customizeRequestInfoDTO.backgroundColor());
        }

        customRepository.deleteAllByUserScrap(userScrap);
        for(CustomDTO.HighlightsInfoDTO highlightsInfoDTO : customizeRequestInfoDTO.highlightsInfos()) {
            customRepository.save(ScrapCustom
                    .builder()
                    .startPoint(highlightsInfoDTO.startPoint())
                    .endPoint(highlightsInfoDTO.endPoint())
                    .highlightsColor(highlightsInfoDTO.highlightsColor())
                    .fontColor(highlightsInfoDTO.highlightsFontColor())
                    .fontSize(highlightsInfoDTO.highlightsFontSize())
                    .isBold(highlightsInfoDTO.isBold())
                    .userScrap(userScrap)
                    .build());
        }

        return Boolean.TRUE;
    }

    public List<ScrapResponseDTO.ScrapInfoDTO> getCustomizeArticles(String token, String category){
        User user = jwtProvider.getUserByToken(token);

        List<ScrapResponseDTO.ScrapInfoDTO> customizeArticles = new ArrayList<>();
        for(UserScrap scrap : user.getUserScraps()){
            if(scrap.getBackgroundColor().equals("white-theme") && scrap.getScrapCustoms().isEmpty()) {
                continue;
            }

            List<Category> categories = new ArrayList<>();
            for(ArticleCategory articleCategory : scrap.getArticle().getArticleCategories()){
                categories.add(articleCategory.getCategory());
            }

            if(!category.equals("전체") && !categories.contains(Category.valueOf(category))){
                continue;
            }

            List<Company> companies = new ArrayList<>();
            for(ArticleSource source : scrap.getArticle().getArticleSources()){
                companies.add(source.getPressCompany());
            }

            List<String> imgUrls = new ArrayList<>();
            for(ArticleImage articleImage: scrap.getArticle().getArticleImages()){
                imgUrls.add(articleImage.getImageUrl());
            }

            customizeArticles.add(ScrapResponseDTO.ScrapInfoDTO
                    .builder()
                    .scrapId(scrap.getId())
                    .isCustomize(true)
                    .title(scrap.getArticle().getTitle())
                    .body(scrap.getArticle().getBody())
                    .categories(categories)
                    .pressCompanies(companies)
                    .imgUrls(imgUrls)
                    .backgroundColor(scrap.getBackgroundColor())
                    .createdAt(scrap.getCreatedAt())
                    .build());
        }
        return customizeArticles;
    }

    public Boolean deleteScrap(List<Long> scrapIds){
        for(Long scrapId: scrapIds) {
            scrapRepository.deleteById(scrapId);
        }

        return Boolean.TRUE;
    }

    public Boolean deleteCustomize(List<Long> scrapIds){
        for(Long scrapId: scrapIds) {
            customRepository.deleteAllByUserScrap(scrapRepository.findById(scrapId).get());
        }

        return Boolean.TRUE;
    }

}
