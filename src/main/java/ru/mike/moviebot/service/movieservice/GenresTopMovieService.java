package ru.mike.moviebot.service.movieservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mike.moviebot.api.tmdb.enrich.realisation.GenresTopMovieEnricher;
import ru.mike.moviebot.dto.Movie;

import java.util.List;

@Service
public class GenresTopMovieService {
    @Autowired
    private GenresTopMovieEnricher genresTopMovieEnricher;

    public List<Movie> findGenresTopMoviesByGenre(int genre, Integer page) {
        return genresTopMovieEnricher.enrichGenresTopMoviesByGenre(genre, page);
    }

    public List<Movie> findGenresTopMoviesByYear(Integer year, Integer page) {
        return genresTopMovieEnricher.enrichGenresTopMoviesByYear(year, page);
    }

    public List<Movie> findGenresTopMoviesByRating(Integer page) {
        return genresTopMovieEnricher.enrichGenresTopMoviesByRating(page);
    }
}
