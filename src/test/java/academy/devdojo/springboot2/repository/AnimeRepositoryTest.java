package academy.devdojo.springboot2.repository;

import academy.devdojo.springboot2.domain.Anime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

import static academy.devdojo.springboot2.util.AnimeUtil.createAnimeToBeSave;


@DataJpaTest
@DisplayName("Anime Repository Tests")
class AnimeRepositoryTest {
    @Autowired
    private AnimeRepository animeRepository;

    @Test
    @DisplayName("Save creates anime when successful")
    public void save_PersistAnime_WhenSuccessful() {
        Anime anime = createAnimeToBeSave();
        Anime savedAnime = this.animeRepository.save(anime);

        Assertions.assertThat(savedAnime.getId()).isNotNull();
        Assertions.assertThat(savedAnime.getName()).isNotNull();
        Assertions.assertThat(savedAnime.getName()).isEqualTo(anime.getName());
        Assertions.assertThat(savedAnime.getUrl()).isNotNull();
        Assertions.assertThat(savedAnime.getUrl()).isEqualTo(anime.getUrl());
    }

    @Test
    @DisplayName("Save updates anime when successful")
    public void save_UpdateAnime_WhenSuccessful() {
        Anime anime = createAnimeToBeSave();
        Anime savedAnime = this.animeRepository.save(anime);

        savedAnime.setName("Anime Test Update");
        savedAnime.setUrl("http://animetesteupdate.com");

        Anime updatedAnime = this.animeRepository.save(savedAnime);

        Assertions.assertThat(savedAnime.getId()).isNotNull();
        Assertions.assertThat(savedAnime.getName()).isNotNull();
        Assertions.assertThat(savedAnime.getName()).isEqualTo(updatedAnime.getName());
        Assertions.assertThat(savedAnime.getUrl()).isNotNull();
        Assertions.assertThat(savedAnime.getUrl()).isEqualTo(updatedAnime.getUrl());
    }

    @Test
    @DisplayName("Delete removes anime when successful")
    public void delete_RemoveAnime_WhenSuccessful() {
        Anime anime = createAnimeToBeSave();
        Anime savedAnime = this.animeRepository.save(anime);
        this.animeRepository.delete(anime);
        Optional<Anime> animeOptional = this.animeRepository.findById(savedAnime.getId());

        Assertions.assertThat(animeOptional.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Find by name returns anime when successful")
    public void findByName_ReturnAnimes_WhenSuccessful() {
        Anime anime = createAnimeToBeSave();
        Anime savedAnime = this.animeRepository.save(anime);

        String name = savedAnime.getName();

        List<Anime> animeList = this.animeRepository.findByName(name);

        Assertions.assertThat(animeList).isNotEmpty();
        Assertions.assertThat(animeList).contains(savedAnime);
    }

    @Test
    @DisplayName("Find by name returns empty list when no anime is found")
    public void findByName_ReturnEmptyList_WhenAnimeNotFound() {
        String name = "fake-name";
        List<Anime> animeList = this.animeRepository.findByName(name);
        Assertions.assertThat(animeList).isEmpty();
    }

    @Test
    @DisplayName("Save throw ConstraintViolationException when name is empty")
    public void save_ThrowsConstraintViolationException_WhenNameIsEmpty() {

        Anime anime = new Anime();
       // Assertions.assertThatThrownBy(()->animeRepository.save(anime))
         //       .isInstanceOf(ConstraintViolationException.class);

        Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(()->this.animeRepository.save(anime))
                .withMessageContaining("The name of this anime cannot be empty.");

    }
}