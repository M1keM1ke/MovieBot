package ru.mike.moviebot.service.movieservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mike.moviebot.api.tmdb.enrich.realisation.RandomMessageEnricher;
import ru.mike.moviebot.dto.Movie;

@Service
public class RandomMovieService {

    @Autowired
    private RandomMessageEnricher randomMessageEnricher;

    public Movie getRandomMovie() {
        return randomMessageEnricher.enrichRandomMovie();
    }
}
