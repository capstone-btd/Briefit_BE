//package capstone.briefit.scheduler;
//
//import capstone.briefit.domain.Wordcloud;
//import capstone.briefit.repository.WordcloudRepository;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.transaction.Transactional;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import org.springframework.web.reactive.function.client.WebClient;
//
//@Component
//public class WordcloudScheduler {
//    private final WebClient webClient = WebClient.builder().baseUrl("https://extractor-api-29633585474.asia-southeast1.run.app").build();
//    private static WordcloudRepository wordcloudRepository;
//
//    @Autowired
//    public WordcloudScheduler(WordcloudRepository wordcloudRepository) {
//        this.wordcloudRepository = wordcloudRepository;
//    }
//
//    // 오전 6시, 오후 6시에 실행
//    @Scheduled(cron = "0 0 6,18 * * *") // 초 분 시 일 월 요일 순서
//    public void saveWordcloud() {
//        String response = webClient.get().uri("/trends/korea").retrieve().bodyToMono(String.class).block(); // 동기 방식으로 응답 대기
//
//        try {
//            ObjectMapper objectMapper = new ObjectMapper();
//            JsonNode wordcloud = objectMapper.readTree(response);
//
//            wordcloudRepository.deleteAll();
//            for (JsonNode word : wordcloud) {
//                wordcloudRepository.save(Wordcloud.builder().word(word.path("keyword").asText()).score(word.path("search_volume").asInt()).build());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
