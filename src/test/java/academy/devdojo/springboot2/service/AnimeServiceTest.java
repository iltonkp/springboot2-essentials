package academy.devdojo.springboot2.service;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.exception.ResourceNotFoundException;
import academy.devdojo.springboot2.repository.AnimeRepository;
import academy.devdojo.springboot2.util.AnimeUtil;
import academy.devdojo.springboot2.util.Utils;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;


@ExtendWith(SpringExtension.class)
class AnimeServiceTest {

    @InjectMocks
    private AnimeService animeService;

    @Mock
    private AnimeRepository animeRepositoryMock;

    @Mock
    private Utils utilsMock;

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

        BDDMockito.when(utilsMock.findAnimeOrThrowNotFound(ArgumentMatchers.anyInt(), ArgumentMatchers.any(AnimeRepository.class)))
            .thenReturn(AnimeUtil.createValidAnime());
    }

    @Test
    @DisplayName("listAll returns a pageable list of animes when successful")
    public void listAll_ReturnListOfAnimeInsidePageObject_WhenSuccessful(){

        String expectedName = AnimeUtil.createValidAnime().getName();
        Page<Anime> animePage = animeService.listAll(PageRequest.of(1,1));

        Assertions.assertThat(animePage).isNotNull();

        Assertions.assertThat(animePage.toList()).isNotEmpty();

        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);

    }

    @Test
    @DisplayName("findByID returns an anime  when successful")
    public void findById_ReturnAnAnime_WhenSuccessful(){

        Integer expectedId = AnimeUtil.createValidAnime().getId();

        Anime anime = animeService.findById(1);

        Assertions.assertThat(anime).isNotNull();

        Assertions.assertThat(anime.getId()).isNotNull();

        Assertions.assertThat(anime.getId()).isEqualTo(expectedId);

    }

    @Test
    @DisplayName("findByName returns a list of  anime  when successful")
    public void findById_ReturnsAListOfAnime_WhenSuccessful(){

        String expectedName = AnimeUtil.createValidAnime().getName();
        List<Anime> animeList = animeService.findByName("DBZ");

        Assertions.assertThat(animeList).isNotNull();

        Assertions.assertThat(animeList.get(0).getName()).isNotNull();

        Assertions.assertThat(animeList.get(0).getName()).isEqualTo(expectedName);

    }

    @Test
    @DisplayName("save creates an anime when successful")
    public void save_CreatesAnAnime_WhenSuccessful(){

        Integer expectedId = AnimeUtil.createValidAnime().getId();
        Anime animeToBeSave = AnimeUtil.createAnimeToBeSave();

        Anime anime = animeService.save(animeToBeSave);

        Assertions.assertThat(anime).isNotNull();

        Assertions.assertThat(anime.getId()).isNotNull();

        Assertions.assertThat(anime.getId()).isEqualTo(expectedId);

    }

    @Test
    @DisplayName("delete removes the anime when successful")
    public void delete_RemovesTheAnime_WhenSuccessful(){

        Assertions.assertThatCode(()-> animeService.delete(1)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("delete throws ResourceNotFoundException if when the anime does not exist")
    public void delete_ThrowsResourceNotFoundException_WhenAnimeDoesNotExist(){

        BDDMockito.when(utilsMock.findAnimeOrThrowNotFound(ArgumentMatchers.anyInt(), ArgumentMatchers.any(AnimeRepository.class)))
                .thenThrow(new ResourceNotFoundException("Anime not Found"));

        Assertions.assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> animeService.delete(1));
    }

    @Test
    @DisplayName("update save updated anime when successful")
    public void save_SaveUpdatedAnime_WhenSuccessful(){
        Anime validUpdatedAnime = AnimeUtil.createValidUpdatedAnime();
        String expectedName = validUpdatedAnime.getName();

        Anime anime = animeService.save(AnimeUtil.createValidAnime());

        Assertions.assertThat(anime).isNotNull();

        Assertions.assertThat(anime.getName()).isNotNull();

        Assertions.assertThat(anime.getName()).isEqualTo(expectedName);


    }


}