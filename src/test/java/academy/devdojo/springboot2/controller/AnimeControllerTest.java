package academy.devdojo.springboot2.controller;


import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.service.AnimeService;
import academy.devdojo.springboot2.util.AnimeUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
class AnimeControllerTest {

    @InjectMocks
    private  AnimeController animeController;

    @Mock
    private AnimeService animeServiceMok;

    @BeforeEach
    public void setUp(){
        PageImpl<Anime> animePage = new PageImpl<>(List.of(AnimeUtil.createValidAnime()));
        List<Anime> animeList = List.of(AnimeUtil.createValidAnime());

        BDDMockito.when(animeServiceMok.listAll(ArgumentMatchers.any()))
                .thenReturn(animePage);

        BDDMockito.when(animeServiceMok.findById(ArgumentMatchers.anyInt()))
                .thenReturn(AnimeUtil.createValidAnime());

        BDDMockito.when(animeServiceMok.findByName(ArgumentMatchers.anyString()))
                .thenReturn(animeList);

        BDDMockito.when(animeServiceMok.save(AnimeUtil.createAnimeToBeSave()))
                .thenReturn(AnimeUtil.createValidAnime());

        BDDMockito.doNothing().when(animeServiceMok).delete(ArgumentMatchers.anyInt());

        BDDMockito.when(animeServiceMok.save(AnimeUtil.createValidAnime()))
                .thenReturn(AnimeUtil.createValidUpdatedAnime());

    }

    @Test
    @DisplayName("listAll returns a pageable list of animes when successful")
    public void listAll_ReturnListOfAnimeInsidePageObject_WhenSuccessful(){

        String expectedName = AnimeUtil.createValidAnime().getName();
        Page<Anime> animePage = animeController.listAll(null).getBody();

        Assertions.assertThat(animePage).isNotNull();

        Assertions.assertThat(animePage.toList()).isNotEmpty();

        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);

    }

    @Test
    @DisplayName("findByID returns an anime  when successful")
    public void findById_ReturnAnAnime_WhenSuccessful(){

        Integer expectedId = AnimeUtil.createValidAnime().getId();

        Anime anime = animeController.findById(1).getBody();

        Assertions.assertThat(anime).isNotNull();

        Assertions.assertThat(anime.getId()).isNotNull();

        Assertions.assertThat(anime.getId()).isEqualTo(expectedId);

    }

    @Test
    @DisplayName("findByName returns a list of  anime  when successful")
    public void findById_ReturnsAListOfAnime_WhenSuccessful(){

        String expectedName = AnimeUtil.createValidAnime().getName();
        List<Anime> animeList = animeController.findByName("DBZ").getBody();

        Assertions.assertThat(animeList).isNotNull();

        Assertions.assertThat(animeList.get(0).getName()).isNotNull();

        Assertions.assertThat(animeList.get(0).getName()).isEqualTo(expectedName);

    }

    @Test
    @DisplayName("save creates an anime when successful")
    public void save_CreatesAnAnime_WhenSuccessful(){

        Integer expectedId = AnimeUtil.createValidAnime().getId();
        Anime animeToBeSave = AnimeUtil.createAnimeToBeSave();

        Anime anime = animeController.save(animeToBeSave).getBody();

        Assertions.assertThat(anime).isNotNull();

        Assertions.assertThat(anime.getId()).isNotNull();

        Assertions.assertThat(anime.getId()).isEqualTo(expectedId);

    }

    @Test
    @DisplayName("delete removes the anime when successful")
    public void delete_RemovesTheAnime_WhenSuccessful(){

        ResponseEntity<Void> responseEntity = animeController.delete(1);

        Assertions.assertThat(responseEntity).isNotNull();

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        Assertions.assertThat(responseEntity.getBody()).isNull();

    }

    @Test
    @DisplayName("update save updated anime when successful")
    public void update_SaveUpdatedAnime_WhenSuccessful(){
        Anime validUpdatedAnime = AnimeUtil.createValidUpdatedAnime();
        String expectedName = validUpdatedAnime.getName();

         ResponseEntity<Void> responseEntity =  animeController.update(AnimeUtil.createValidAnime());

        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        Assertions.assertThat(responseEntity.getBody()).isNull();




    }

}