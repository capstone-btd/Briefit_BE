package capstone.briefit.controller;

import capstone.briefit.dto.WordcloudResponseDTO;
import capstone.briefit.service.WordService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/word")
public class WordController {
    private static WordService wordService;

    @Autowired
    public WordController(WordService wordService) {
        this.wordService = wordService;
    }

    @GetMapping("/wordcloud")
    public WordcloudResponseDTO.WordcloudInfoDTO getWordcloud(){
        return wordService.getWordcloud();
    }

    @GetMapping("")
    public Map<String, Object> getWordMeaning(@RequestParam String string){
        return wordService.getWordMeaning(string);
    }
}
