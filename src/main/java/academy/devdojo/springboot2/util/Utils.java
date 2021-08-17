package academy.devdojo.springboot2.util;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.exception.ResourceNotFoundException;
import academy.devdojo.springboot2.repository.AnimeRepository;
import org.springframework.stereotype.Component;



@Component
public class Utils {

    public Anime findAnimeOrThrowNotFound(int id, AnimeRepository animeRepository){
        return  animeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Anime Not Found"));
    }

}
