package academy.devdojo.springboot2.util;

import academy.devdojo.springboot2.domain.Anime;

public class AnimeUtil {

    public static Anime createAnimeToBeSave() {
        return Anime.builder()
                .name("Anime Teste")
                .url("http://animeteste.com")
                .build();
    }

    public static Anime createValidAnime() {
        return Anime.builder()
                .id(1)
                .name("Anime Teste")
                .url("http://animeteste.com")
                .build();
    }

    public static Anime createValidUpdatedAnime() {
        return Anime.builder()
                .id(1)
                .name("Anime Teste 2")
                .url("http://animeteste2.com")
                .build();
    }
}
