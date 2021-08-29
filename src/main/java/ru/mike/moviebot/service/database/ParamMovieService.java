package ru.mike.moviebot.service.database;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.mike.moviebot.api.tmdb.enrich.realisation.ParamMovieEnricher;
import ru.mike.moviebot.domain.param.ParamMovie;
import ru.mike.moviebot.dto.Movie;
import ru.mike.moviebot.exception.EntityNotFoundException;
import ru.mike.moviebot.repository.ParamMovieRepo;
import ru.mike.moviebot.service.common.TelegramService;

import java.util.List;

@Service
@Slf4j
public class ParamMovieService {
    @Autowired
    ParamMovieRepo paramMovieRepo;
    @Autowired
    private ParamMovieEnricher paramMovieEnricher;

    public ParamMovie findParamMovieByChatId(Long chatId) {
        return paramMovieRepo.findParamMovieById(chatId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "ParamMovie entity with chatId=" + chatId + "not found!"
                ));
    }

    public List<Movie> findCustomMovies(ParamMovie paramMovie) {
        return paramMovieEnricher.enrichCustomMovies(paramMovie);
    }

    public ParamMovie disableParamEntity(Update update) {
        ParamMovie paramMovie = findParamMovieByChatId(TelegramService.getMessage(update).getChatId());

        paramMovie.setMovieNum(0);
        paramMovie.setPage(1);
        paramMovieRepo.save(paramMovie);
        log.info(String.format("Disable %s entity was successful", ParamMovie.class.getName()));
        return paramMovie;
    }

    public void saveParamMovie(@NonNull ParamMovie paramMovie) {
        paramMovieRepo.save(paramMovie);
        log.info("ParamMovie entity with id=" + paramMovie.getId() + " saved.");
    }

    public ParamMovie disableActor(@NonNull ParamMovie paramMovie) {
        paramMovie.setActorId(null);
        paramMovie.setActor(null);
        saveParamMovie(paramMovie);
        log.info("Actor in ParamMovie entity with id=" + paramMovie.getId() + " disabled.");
        return paramMovie;
    }
}
