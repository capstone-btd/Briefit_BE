package capstone.briefit.service;

import capstone.briefit.domain.Wordcloud;
import capstone.briefit.dto.WordcloudResponseDTO;
import capstone.briefit.repository.WordcloudRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class WordcloudService {
    private static WordcloudRepository wordcloudRepository;

    @Autowired
    public WordcloudService(WordcloudRepository wordcloudRepository) {
        this.wordcloudRepository = wordcloudRepository;
    }

    public WordcloudResponseDTO.WordcloudInfoDTO getWordcloud(){
        List<Wordcloud> wordclouds = wordcloudRepository.findAll();
        List<Map<String, Object>> words = new ArrayList<>();

        for(Wordcloud wordcloud : wordclouds){
            Map<String, Object> word = new HashMap<>();
            word.put("word", wordcloud.getWord());
            word.put("score", wordcloud.getScore());
            words.add(word);
        }

        return WordcloudResponseDTO.WordcloudInfoDTO
                .builder()
                .createdAt(wordclouds.get(0).getCreatedAt())
                .words(words)
                .build();
    }
}
