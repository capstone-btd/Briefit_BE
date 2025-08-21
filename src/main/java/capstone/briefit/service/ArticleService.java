package capstone.briefit.service;

import capstone.briefit.config.JwtProvider;
import capstone.briefit.domain.*;
import capstone.briefit.domain.enums.Category;
import capstone.briefit.domain.enums.Company;
import capstone.briefit.dto.ArticleResponseDTO;
import capstone.briefit.dto.CustomDTO;
import capstone.briefit.dto.SourceDTO;
import capstone.briefit.repository.ArticleRepository;
import capstone.briefit.repository.CustomInfoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ArticleService {
    private static ArticleRepository articleRepository;
    private static CustomInfoRepository customInfoRepository;
    private static JwtProvider jwtProvider;

    @Autowired
    public ArticleService(ArticleRepository articleRepository, CustomInfoRepository customInfoRepository, JwtProvider jwtProvider) {
        this.articleRepository = articleRepository;
        this.customInfoRepository = customInfoRepository;
        this.jwtProvider = jwtProvider;
    }

    public List<ArticleResponseDTO.ArticleInfoDTO> getArticles(String token, String category){
        List<ArticleResponseDTO.ArticleInfoDTO> articleInfos = new ArrayList<>();

        User user = null;
        if(token != null) {
            user = jwtProvider.getUserByToken(token);
        }

        List<Article> articles = new ArrayList<>();
        if(category.equals("전체")){
            articles = articleRepository.findAll();
        }else{
            articles = articleRepository.findByCategory(category);
        }

        for(Article article : articles){
            List<Category> categories = new ArrayList<>();
            for(ArticleCategory articleCategory : article.getArticleCategories()){
                categories.add(articleCategory.getCategory());
            }

            List<Company> companies = new ArrayList<>();
            for(ArticleSource source : article.getArticleSources()){
                companies.add(source.getPressCompany());
            }

            List<String> imgUrls = new ArrayList<>();
            for(ArticleImage articleImage: article.getArticleImages()){
                imgUrls.add(articleImage.getImageUrl());
            }

            Long scrapId = null;
            Long customId = null;
            String backgroundColor = null;
            if(user != null) {
                for (UserScrap userScrap : user.getUserScraps()){
                    if(article.getId().equals(userScrap.getArticle().getId())){
                        scrapId = userScrap.getId();
                    }
                }
                for (UserCustom userCustom : user.getUserCustoms()){
                    if(article.getId().equals(userCustom.getArticle().getId())){
                        customId = userCustom.getId();
                        backgroundColor = userCustom.getBackgroundColor();
                    }
                }
            }

            articleInfos.add(ArticleResponseDTO.ArticleInfoDTO
                    .builder()
                    .articleId(article.getId())
                    .scrapId(scrapId)
                    .customId(customId)
                    .title(article.getTitle())
                    .body(article.getBody())
                    .categories(categories)
                    .pressCompanies(companies)
                    .imgUrls(imgUrls)
                    .backgroundColor(backgroundColor)
                    .createdAt(article.getCreatedAt())
                    .build());
        }

        return articleInfos;
    }

    public List<ArticleResponseDTO.ArticleInfoDTO> recommendArticles(String token, String category){
        List<Article> articles = new ArrayList<>();
        List<ArticleResponseDTO.ArticleInfoDTO> articleInfos = new ArrayList<>();

        User user = jwtProvider.getUserByToken(token);
        List<String> tags = new ArrayList<>();
        for(UserTag tag : user.getUserTags()) {
            tags.add(tag.getTag().toString());
        }

        if(category.equals("전체")){
            articles = articleRepository.findByTags(tags);
        }else{
            articles = articleRepository.findByTagsAndCategory(tags, category);
        }

        for(Article article : articles){
            List<Category> categories = new ArrayList<>();
            for(ArticleCategory articleCategory : article.getArticleCategories()){
                categories.add(articleCategory.getCategory());
            }

            List<Company> companies = new ArrayList<>();
            for(ArticleSource source : article.getArticleSources()){
                companies.add(source.getPressCompany());
            }

            List<String> imgUrls = new ArrayList<>();
            for(ArticleImage articleImage: article.getArticleImages()){
                imgUrls.add(articleImage.getImageUrl());
            }

            Long scrapId = null;
            Long customId = null;
            String backgroundColor = null;
            if(user != null) {
                for (UserScrap userScrap : user.getUserScraps()){
                    if(article.getId().equals(userScrap.getArticle().getId())){
                        scrapId = userScrap.getId();
                    }
                }
                for (UserCustom userCustom : user.getUserCustoms()){
                    if(article.getId().equals(userCustom.getArticle().getId())){
                        customId = userCustom.getId();
                        backgroundColor = userCustom.getBackgroundColor();
                    }
                }
            }

            articleInfos.add(ArticleResponseDTO.ArticleInfoDTO
                    .builder()
                    .articleId(article.getId())
                    .scrapId(scrapId)
                    .customId(customId)
                    .title(article.getTitle())
                    .body(article.getBody())
                    .categories(categories)
                    .pressCompanies(companies)
                    .imgUrls(imgUrls)
                    .backgroundColor(backgroundColor)
                    .createdAt(article.getCreatedAt())
                    .build());
        }

        return articleInfos;
    }

    public ArticleResponseDTO.ArticleDetailInfoDTO getArticle(String token, Long id){
        User user = null;
        if(token != null) {
            user = jwtProvider.getUserByToken(token);
        }

        Article article = articleRepository.findById(id).get();
        Long scrapId = null;
        Long customId = null;
        String backgroundColor = null;
        List<CustomDTO.CustomInfoDTO> customs = new ArrayList<>();
        if(user != null) {
            for (UserScrap userScrap : user.getUserScraps()){
                if(article.getId().equals(userScrap.getArticle().getId())){
                    scrapId = userScrap.getId();
                }
            }

            for (UserCustom userCustom : user.getUserCustoms()){
                if(article.getId().equals(userCustom.getArticle().getId())){
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
        }

        List<SourceDTO.SourceInfoDTO> sources = new ArrayList<>();
        for(ArticleSource source : article.getArticleSources()) {
            sources.add(SourceDTO.SourceInfoDTO
                    .builder()
                    .url(source.getUrl())
                    .pressCompany(source.getPressCompany())
                    .sourceTitle(source.getTitle())
                    .build());
        }

        List<Category> categories = new ArrayList<>();
        for(ArticleCategory articleCategory : article.getArticleCategories()){
            categories.add(articleCategory.getCategory());
        }

        List<String> imgUrls = new ArrayList<>();
        for(ArticleImage articleImage: article.getArticleImages()){
            imgUrls.add(articleImage.getImageUrl());
        }

        return ArticleResponseDTO.ArticleDetailInfoDTO
                .builder()
                .articleId(article.getId())
                .scrapId(scrapId)
                .customId(customId)
                .title(article.getTitle())
                .body(article.getBody())
                .categories(categories)
                .sources(sources)
                .imgUrls(imgUrls)
                .backgroundColor(backgroundColor)
//                .fontSize(null)
//                .fontColor(null)
                .customs(customs)
                .createdAt(article.getCreatedAt())
                .build();
    }

    public List<ArticleResponseDTO.ArticleInfoDTO> searchArticles(String token, String string) {
        List<ArticleResponseDTO.ArticleInfoDTO> articleInfos = new ArrayList<>();

        User user = null;
        if(!(token == null)) {
            user = jwtProvider.getUserByToken(token);
        }

        List<Article> articles = articleRepository.findByTitleContainingOrBodyContaining(string, string);
        for(Article article : articles){
            List<Category> categories = new ArrayList<>();
            for(ArticleCategory articleCategory : article.getArticleCategories()){
                categories.add(articleCategory.getCategory());
            }

            List<Company> companies = new ArrayList<>();
            for(ArticleSource source : article.getArticleSources()){
                companies.add(source.getPressCompany());
            }

            List<String> imgUrls = new ArrayList<>();
            for(ArticleImage articleImage: article.getArticleImages()){
                imgUrls.add(articleImage.getImageUrl());
            }

            Long scrapId = null;
            Long customId = null;
            String backgroundColor = null;
            if(user != null) {
                for (UserScrap userScrap : user.getUserScraps()){
                    if(article.getId().equals(userScrap.getArticle().getId())){
                        scrapId = userScrap.getId();
                    }
                }
                for (UserCustom userCustom : user.getUserCustoms()){
                    if(article.getId().equals(userCustom.getArticle().getId())){
                        customId = userCustom.getId();
                        backgroundColor = userCustom.getBackgroundColor();
                    }
                }
            }

            articleInfos.add(ArticleResponseDTO.ArticleInfoDTO
                    .builder()
                    .articleId(article.getId())
                    .scrapId(scrapId)
                    .customId(customId)
                    .title(article.getTitle())
                    .body(article.getBody())
                    .categories(categories)
                    .pressCompanies(companies)
                    .imgUrls(imgUrls)
                    .backgroundColor(backgroundColor)
                    .createdAt(article.getCreatedAt())
                    .build());
        }

        return articleInfos;
    }

}
