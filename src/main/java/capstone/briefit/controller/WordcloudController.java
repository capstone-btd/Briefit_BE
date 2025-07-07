package capstone.briefit.controller;

import capstone.briefit.dto.WordcloudResponseDTO;
import capstone.briefit.service.WordcloudService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wordcloud")
public class WordcloudController {
    WordcloudService wordcloudService;

    @Autowired
    public WordcloudController(WordcloudService wordcloudService) {
        this.wordcloudService = wordcloudService;
    }

    @GetMapping("")
    public WordcloudResponseDTO.WordcloudInfoDTO getWordcloud(){
        return wordcloudService.getWordcloud();
    }

}
