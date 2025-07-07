package capstone.briefit.service;

import capstone.briefit.config.JwtProvider;
import capstone.briefit.domain.*;
import capstone.briefit.domain.enums.Category;
import capstone.briefit.domain.enums.Company;
import capstone.briefit.dto.ArticleResponseDTO;
import capstone.briefit.dto.SourceDTO;
import capstone.briefit.repository.ArticleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ArticleService {
    private static ArticleRepository articleRepository;
    private static JwtProvider jwtProvider;

    @Autowired
    public ArticleService(ArticleRepository articleRepository, JwtProvider jwtProvider) {
        this.articleRepository = articleRepository;
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
            boolean isCustomize = false;
            if(user != null) {
                for (UserScrap userScrap : user.getUserScraps()) {
                    if (userScrap.getArticle().getId().equals(article.getId())) {
                        scrapId = userScrap.getId();
                        if (!userScrap.getBackgroundColor().equals("white-theme") || !userScrap.getScrapCustoms().isEmpty()) {
                            isCustomize = true;
                        }
                    }
                }
            }

            articleInfos.add(ArticleResponseDTO.ArticleInfoDTO
                    .builder()
                    .articleId(article.getId())
                    .scrapId(scrapId)
                    .isCustomize(isCustomize)
                    .title(article.getTitle())
                    .body(article.getBody())
                    .categories(categories)
                    .pressCompanies(companies)
                    .imgUrls(imgUrls)
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
            boolean isCustomize = false;
            for (UserScrap userScrap : user.getUserScraps()) {
                if (userScrap.getArticle().getId().equals(article.getId())) {
                    scrapId = userScrap.getId();
                    if (!userScrap.getBackgroundColor().equals("white-theme") || !userScrap.getScrapCustoms().isEmpty()) {
                        isCustomize = true;
                    }
                }
            }

            articleInfos.add(ArticleResponseDTO.ArticleInfoDTO
                    .builder()
                    .articleId(article.getId())
                    .scrapId(scrapId)
                    .isCustomize(isCustomize)
                    .title(article.getTitle())
                    .body(article.getBody())
                    .categories(categories)
                    .pressCompanies(companies)
                    .imgUrls(imgUrls)
                    .createdAt(article.getCreatedAt())
                    .build());
        }

        return articleInfos;
    }

    public ArticleResponseDTO.ArticleDetailInfoDTO getArticle(Long id){
        Article article = articleRepository.findById(id).get();

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
                .title(article.getTitle())
                .body(article.getBody())
                .categories(categories)
                .sources(sources)
                .imgUrls(imgUrls)
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
            boolean isCustomize = false;
            if(user != null) {
                for (UserScrap userScrap : user.getUserScraps()) {
                    if (userScrap.getArticle().getId().equals(article.getId())) {
                        scrapId = userScrap.getId();
                        if (!userScrap.getBackgroundColor().equals("white-theme") || !userScrap.getScrapCustoms().isEmpty()) {
                            isCustomize = true;
                        }
                    }
                }
            }

            articleInfos.add(ArticleResponseDTO.ArticleInfoDTO
                    .builder()
                    .articleId(article.getId())
                    .scrapId(scrapId)
                    .isCustomize(isCustomize)
                    .title(article.getTitle())
                    .body(article.getBody())
                    .categories(categories)
                    .pressCompanies(companies)
                    .imgUrls(imgUrls)
                    .createdAt(article.getCreatedAt())
                    .build());
        }

        return articleInfos;
    }

}
