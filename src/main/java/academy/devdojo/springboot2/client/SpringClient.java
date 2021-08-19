package academy.devdojo.springboot2.client;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.wrapper.PageableResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class SpringClient {

    public static void main(String[] args) {
        /*
        ResponseEntity<Anime> animeResponseEntity = new RestTemplate().getForEntity("http://localhost:8080/animes/{id}", Anime.class, 1);
        log.info("Response Entity {}", animeResponseEntity);
        log.info("Response Data {}", animeResponseEntity.getBody());

        Anime anime = new  RestTemplate().getForObject("http://localhost:8080/animes/{id}", Anime.class, 1);
        log.info("Anime {}", anime);


        Anime[] animeArray = new  RestTemplate().getForObject("http://localhost:8080/animes", Anime[].class);
        log.info("Anime Array {}", Arrays.toString(animeArray));

        ResponseEntity<List<Anime>> exchangeList =  new  RestTemplate()
                .exchange("http://localhost:8080/animes", HttpMethod.GET, null, new ParameterizedTypeReference<List<Anime>>() {});
        log.info("Anime List {}", exchangeList.getBody());
        */

        ResponseEntity<PageableResponse<Anime>> exchangeList =  new  RestTemplate()
                .exchange("http://localhost:8080/animes", HttpMethod.GET, null, new ParameterizedTypeReference<PageableResponse<Anime>>() {});

        log.info("Anime List {}", exchangeList.getBody());

        Anime overlord = Anime.builder().name("OverLord").url("http://overlord.com").build();
        Anime overlordSave = new RestTemplate().postForObject("http://localhost:8080/animes", overlord, Anime.class);
        log.info("Overlord Save ID:  {}", overlordSave.getId());

        Anime stainsGate = Anime.builder().name("Stains Gate").url("http://stainsGate.com").build();
        Anime stainsGateSave = new RestTemplate().exchange("http://localhost:8080/animes", HttpMethod.POST, new HttpEntity<>(stainsGate, createJsonHeader()), Anime.class ).getBody();
        log.info("Stains Gate Save ID:  {}", stainsGateSave.getId());

        stainsGateSave.setName("Stains Gate Zero");
        ResponseEntity<Void> updatedSteinsGate =  new RestTemplate().exchange("http://localhost:8080/animes", HttpMethod.PUT, new HttpEntity<>(stainsGateSave, createJsonHeader()), Void.class);
        log.info("Stains Gates updated status:  {}", updatedSteinsGate.getStatusCode());

        ResponseEntity<Void> updatedSteinsGateDelete =  new RestTemplate().exchange("http://localhost:8080/animes/{id}", HttpMethod.DELETE, null , Void.class, stainsGateSave.getId());
        log.info("Stains Gates deleted status:  {}", updatedSteinsGateDelete.getStatusCode());

    }
    private static HttpHeaders createJsonHeader(){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        //... put anythings here.
        return httpHeaders;
    }
}
