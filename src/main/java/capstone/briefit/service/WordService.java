package capstone.briefit.service;

import capstone.briefit.domain.Wordcloud;
import capstone.briefit.dto.WordcloudResponseDTO;
import capstone.briefit.repository.WordcloudRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class WordService {
    private static WordcloudRepository wordcloudRepository;

    @Autowired
    public WordService(WordcloudRepository wordcloudRepository) {
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

    public Map<String, Object> getWordMeaning(String string) {
        WebClient webClient = WebClient.builder().baseUrl("https://krdict.korean.go.kr").build();

        String response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                                .path("/api/search")
                                .queryParam("key", "4099F74EDCFA5963F045B327ECE753D6") // 실제 발급받은 키로 대체
                                .queryParam("q", string)       // 검색어
                                .build())
                .retrieve()
                .bodyToMono(String.class)
                .block(); // 동기 방식

        Map<String, Object> meaning = new HashMap<>();
        meaning.put("word", string);
        try {
            // XML을 JsonNode로 변환
            XmlMapper xmlMapper = new XmlMapper();
            JsonNode root = xmlMapper.readTree(response);
            System.out.println(root);

            JsonNode word = null;
            if(root.path("total").asInt() > 1){
                word = root.path("item").get(0);
            }else{
                word = root.path("item");
            }
            meaning.put("pos", word.path("pos").asText());

            List<String> definitions = new ArrayList<>();
            if(word.path("sense").isArray()){
                int i = 1;
                for(JsonNode sense : word.path("sense")){
                   definitions.add(String.valueOf(i) + ". " + sense.path("definition").asText());
                   i++;
                }
            }else{
                definitions.add(word.path("sense").path("definition").asText());
            }
            meaning.put("definition", definitions);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return meaning;
    }

}
