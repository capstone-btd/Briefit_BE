package capstone.briefit.service;

import capstone.briefit.config.JwtProvider;
import capstone.briefit.domain.*;
import capstone.briefit.domain.enums.Category;
import capstone.briefit.domain.enums.Company;
import capstone.briefit.dto.ArticleResponseDTO;
import capstone.briefit.dto.CustomDTO;
import capstone.briefit.dto.ScrapResponseDTO;
import capstone.briefit.repository.ArticleRepository;
import capstone.briefit.repository.CustomInfoRepository;
import capstone.briefit.repository.CustomRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CustomService {
    private final CustomRepository customRepository;
    private final CustomInfoRepository customInfoRepository;
    private final ArticleRepository articleRepository;
    private final JwtProvider jwtProvider;

    @Autowired
    public CustomService(CustomRepository customRepository, CustomInfoRepository customInfoRepository, ArticleRepository articleRepository, JwtProvider jwtProvider) {
        this.customRepository = customRepository;
        this.customInfoRepository = customInfoRepository;
        this.articleRepository = articleRepository;
        this.jwtProvider = jwtProvider;
    }

    public Long customizeArticle(String token, Long articleId, CustomDTO.CustomizeRequestDTO customizeRequestInfoDTO) {
        User user = jwtProvider.getUserByToken(token);
        Article article = articleRepository.findById(articleId).get();

        UserCustom userCustom = null;
        if(customRepository.findByUserAndArticle(user, article).isEmpty()){
            userCustom = customRepository.save(UserCustom.builder().user(user).article(article).backgroundColor(customizeRequestInfoDTO.backgroundColor()).build());
        }else{
            userCustom = customRepository.findByUserAndArticle(user, article).get();
            customInfoRepository.deleteAllByUserCustom(userCustom);
        }

        for(CustomDTO.CustomInfoDTO customInfoDTO : customizeRequestInfoDTO.customInfos()) {
            customInfoRepository.save(CustomInfo
                    .builder()
                    .userCustom(userCustom)
                    .startPoint(customInfoDTO.startPoint())
                    .endPoint(customInfoDTO.endPoint())
                    .highlightsColor(customInfoDTO.highlightsColor())
                    .fontColor(customInfoDTO.highlightsFontColor())
                    .fontSize(customInfoDTO.highlightsFontSize())
                    .isBold(customInfoDTO.isBold())
                    .build());
        }

        return userCustom.getId();
    }

    public List<ArticleResponseDTO.ArticleInfoDTO> getCustomArticles(String token, String category){
        User user = jwtProvider.getUserByToken(token);

        List<ArticleResponseDTO.ArticleInfoDTO> customArticles = new ArrayList<>();
        for(UserCustom userCustom : user.getUserCustoms()){
            List<Category> categories = new ArrayList<>();
            for(ArticleCategory articleCategory : userCustom.getArticle().getArticleCategories()){
                categories.add(articleCategory.getCategory());
            }

            if(!category.equals("전체") && !categories.contains(Category.valueOf(category))){
                continue;
            }

            List<Company> companies = new ArrayList<>();
            for(ArticleSource source : userCustom.getArticle().getArticleSources()){
                companies.add(source.getPressCompany());
            }

            List<String> imgUrls = new ArrayList<>();
            for(ArticleImage articleImage: userCustom.getArticle().getArticleImages()){
                imgUrls.add(articleImage.getImageUrl());
            }

            Long scrapId = null;
            for(UserScrap userScrap : user.getUserScraps()){
                if(userCustom.getArticle().getId().equals(userScrap.getArticle().getId())){
                    scrapId = userScrap.getId();
                }
            }

            customArticles.add(ArticleResponseDTO.ArticleInfoDTO
                    .builder()
                    .articleId(userCustom.getArticle().getId())
                    .scrapId(scrapId)
                    .customId(userCustom.getId())
                    .title(userCustom.getArticle().getTitle())
                    .body(userCustom.getArticle().getBody())
                    .categories(categories)
                    .pressCompanies(companies)
                    .imgUrls(imgUrls)
                    .backgroundColor(userCustom.getBackgroundColor())
                    .createdAt(userCustom.getArticle().getCreatedAt())
                    .build());
        }
        return customArticles;
    }

    public Boolean deleteCustomArticles(List<Long> customIds){
        for(Long customId: customIds) {
            customInfoRepository.deleteAllByUserCustom(customRepository.findById(customId).get());
            customRepository.deleteById(customId);
        }

        return Boolean.TRUE;
    }
}
