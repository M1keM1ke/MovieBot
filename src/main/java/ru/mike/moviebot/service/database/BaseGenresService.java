package ru.mike.moviebot.service.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mike.moviebot.domain.genre.BaseGenre;
import ru.mike.moviebot.exception.EntityNotFoundException;
import ru.mike.moviebot.repository.BaseGenreRepo;

@Service
public class BaseGenresService {
    @Autowired
    BaseGenreRepo baseGenreRepo;

    public BaseGenre findBaseGenreByName(String genreName) {
        return baseGenreRepo.findByGenreName(genreName)
                .orElseThrow(() -> new EntityNotFoundException(
                        "BaseGenre entity with name=" + genreName + "not found!"
                ));
    }

    public BaseGenre findBaseGenreById(Integer genreId) {
        return baseGenreRepo.findByGenreValue(genreId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "BaseGenre entity with id=" + genreId + "not found!"
                ));
    }
}
