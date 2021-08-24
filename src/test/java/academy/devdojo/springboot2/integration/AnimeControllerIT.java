package academy.devdojo.springboot2.integration;



import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.repository.AnimeRepository;

import academy.devdojo.springboot2.util.AnimeUtil;
import academy.devdojo.springboot2.wrapper.PageableResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.*;


import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AnimeControllerIT {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @LocalServerPort
    private int port;

   @MockBean
    private AnimeRepository animeRepositoryMock;

    @BeforeEach
    public void setUp(){
        PageImpl<Anime> animePage = new PageImpl<>(List.of(AnimeUtil.createValidAnime()));
        List<Anime> animeList = List.of(AnimeUtil.createValidAnime());

        BDDMockito.when(animeRepositoryMock.findAll(ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(animePage);

        BDDMockito.when(animeRepositoryMock.findById(anyInt()))
                .thenReturn(Optional.of(AnimeUtil.createValidAnime()));

        BDDMockito.when(animeRepositoryMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(animeList);

        BDDMockito.when(animeRepositoryMock.save(AnimeUtil.createAnimeToBeSave()))
                .thenReturn(AnimeUtil.createValidAnime());

        BDDMockito.doNothing().when(animeRepositoryMock).delete(ArgumentMatchers.any(Anime.class));

        BDDMockito.when(animeRepositoryMock.save(AnimeUtil.createValidAnime()))
                .thenReturn(AnimeUtil.createValidUpdatedAnime());

    }

    @Test
    @DisplayName("listAll returns a pageable list of animes when successful")
    public void listAll_ReturnListOfAnimeInsidePageObject_WhenSuccessful(){

        String expectedName = AnimeUtil.createValidAnime().getName();

        Page<Anime> animePage = testRestTemplate.exchange("/animes", HttpMethod.GET, null, new ParameterizedTypeReference<PageableResponse<Anime>>() {
        }).getBody();

        Assertions.assertThat(animePage).isNotNull();

        Assertions.assertThat(animePage.toList()).isNotEmpty();

        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);

    }

    @Test
    @DisplayName("findByID returns an anime  when successful")
    public void findById_ReturnAnAnime_WhenSuccessful(){

        Integer expectedId = AnimeUtil.createValidAnime().getId();

        Anime anime = testRestTemplate.getForObject("/animes/1", Anime.class);

        Assertions.assertThat(anime).isNotNull();

        Assertions.assertThat(anime.getId()).isNotNull();

        Assertions.assertThat(anime.getId()).isEqualTo(expectedId);

    }

    @Test
    @DisplayName("findByName returns a list of  anime  when successful")
    public void findById_ReturnsAListOfAnime_WhenSuccessful(){

        String expectedName = AnimeUtil.createValidAnime().getName();
        List<Anime> animeList = testRestTemplate.exchange("/animes/find?name='teste2'", HttpMethod.GET, null, new ParameterizedTypeReference<List<Anime>>() {
        }).getBody();

        Assertions.assertThat(animeList).isNotNull();

        Assertions.assertThat(animeList.get(0).getName()).isNotNull();

        Assertions.assertThat(animeList.get(0).getName()).isEqualTo(expectedName);

    }

    @Test
    @DisplayName("save creates an anime when successful")
    public void save_CreatesAnAnime_WhenSuccessful(){

        Integer expectedId = AnimeUtil.createValidAnime().getId();
        Anime animeToBeSave = AnimeUtil.createAnimeToBeSave();

        Anime anime = testRestTemplate.exchange("/animes", HttpMethod.POST, creatJsonHttpEntity(animeToBeSave), Anime.class).getBody();

        Assertions.assertThat(anime).isNotNull();

        Assertions.assertThat(anime.getId()).isNotNull();

        Assertions.assertThat(anime.getId()).isEqualTo(expectedId);

    }

    @Test
    @DisplayName("delete removes the anime when successful")
    public void delete_RemovesTheAnime_WhenSuccessful(){

        ResponseEntity<Void> responseEntity = testRestTemplate.exchange("/animes/1", HttpMethod.DELETE, null, Void.class);

        Assertions.assertThat(responseEntity).isNotNull();

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        Assertions.assertThat(responseEntity.getBody()).isNull();

    }

    @Test
    @DisplayName("update save updated anime when successful")
    public void update_SaveUpdatedAnime_WhenSuccessful(){
        Anime validUpdatedAnime = AnimeUtil.createValidAnime();
        String expectedName = validUpdatedAnime.getName();

         ResponseEntity<Void> responseEntity =  testRestTemplate.exchange("/animes", HttpMethod.PUT, creatJsonHttpEntity(validUpdatedAnime), Void.class);

        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        Assertions.assertThat(responseEntity.getBody()).isNull();




    }

    private HttpEntity<Anime> creatJsonHttpEntity(Anime anime){
        return new HttpEntity<Anime>(anime, createJsonHeader());
    }

    private static HttpHeaders createJsonHeader(){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        //... put anythings here.
        return httpHeaders;
    }

}