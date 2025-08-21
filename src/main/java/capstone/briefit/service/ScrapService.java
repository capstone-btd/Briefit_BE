package capstone.briefit.service;

import capstone.briefit.config.JwtProvider;
import capstone.briefit.domain.*;
import capstone.briefit.domain.enums.Category;
import capstone.briefit.domain.enums.Company;
import capstone.briefit.dto.ArticleResponseDTO;
import capstone.briefit.dto.CustomDTO;
import capstone.briefit.dto.ScrapResponseDTO;
import capstone.briefit.dto.SourceDTO;
import capstone.briefit.repository.ArticleRepository;
import capstone.briefit.repository.CustomInfoRepository;
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
    private final CustomInfoRepository customInfoRepository;

    @Autowired
    public ScrapService(ScrapRepository scrapRepository, JwtProvider jwtProvider, ArticleRepository articleRepository, CustomRepository customRepository, CustomInfoRepository customInfoRepository) {
        this.scrapRepository = scrapRepository;
        this.jwtProvider = jwtProvider;
        this.articleRepository = articleRepository;
        this.customInfoRepository = customInfoRepository;
    }

    public Long scrapAticle(String token, Long articleId){
        User user = jwtProvider.getUserByToken(token);
        Article article = articleRepository.findById(articleId).get();
        UserScrap userScrap = scrapRepository.save(UserScrap.builder().user(user).article(article).build());
        return userScrap.getId();
    }

    public List<ArticleResponseDTO.ArticleInfoDTO> getScraps(String token, String category){
        User user = jwtProvider.getUserByToken(token);

        List<ArticleResponseDTO.ArticleInfoDTO> userScraps = new ArrayList<>();
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

            Long customId = null;
            String backgroundColor = null;
            for(UserCustom userCustom : user.getUserCustoms()){
                if(scrap.getArticle().getId().equals(userCustom.getArticle().getId())){
                    customId = userCustom.getId();
                    backgroundColor = userCustom.getBackgroundColor();
                }
            }

            userScraps.add(ArticleResponseDTO.ArticleInfoDTO
                    .builder()
                    .articleId(scrap.getArticle().getId())
                    .scrapId(scrap.getId())
                    .customId(customId)
                    .title(scrap.getArticle().getTitle())
                    .body(scrap.getArticle().getBody())
                    .categories(categories)
                    .pressCompanies(companies)
                    .imgUrls(imgUrls)
                    .backgroundColor(backgroundColor)
                    .createdAt(scrap.getArticle().getCreatedAt())
                    .build());
        }
        return userScraps;
    }

    public ArticleResponseDTO.ArticleDetailInfoDTO getScrap(String token, Long scrapId){
        User user = jwtProvider.getUserByToken(token);
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

        List<String> imgUrls = new ArrayList<>();
        for(ArticleImage articleImage: scrap.getArticle().getArticleImages()){
            imgUrls.add(articleImage.getImageUrl());
        }

        Long customId = null;
        String backgroundColor = null;
        List<CustomDTO.CustomInfoDTO> customs = new ArrayList<>();
        for(UserCustom userCustom : user.getUserCustoms()){
            if(scrap.getArticle().getId().equals(userCustom.getArticle().getId())){
                customId = userCustom.getId();
                backgroundColor = userCustom.getBackgroundColor();
                for(CustomInfo customInfo : customInfoRepository.findAllByUserCustom(userCustom)){
                    customs.add(CustomDTO.CustomInfoDTO
                            .builder()
                            .startPoint(customInfo.getStartPoint())
                            .endPoint(customInfo.getEndPoint())
                            .highlightsColor(customInfo.getHighlightsColor())
                            .highlightsFontColor(customInfo.getFontColor())
                            .highlightsFontSize(customInfo.getFontSize())
                            .isBold(customInfo.getIsBold())
                            .build()
                    );
                }
            }
        }

        return ArticleResponseDTO.ArticleDetailInfoDTO
                .builder()
                .articleId(scrap.getArticle().getId())
                .scrapId(scrapId)
                .customId(customId)
                .title(scrap.getArticle().getTitle())
                .body(scrap.getArticle().getBody())
                .categories(categories)
                .sources(sources)
                .imgUrls(imgUrls)
                .backgroundColor(backgroundColor)
//                .fontSize(null)
//                .fontColor(null)
                .customs(customs)
                .createdAt(scrap.getArticle().getCreatedAt())
                .build();
    }

    public Boolean deleteScraps(List<Long> scrapIds){
        for(Long scrapId: scrapIds) {
            scrapRepository.deleteById(scrapId);
        }

        return Boolean.TRUE;
    }
}
