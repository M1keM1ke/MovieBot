package ru.mike.moviebot.api.tmdb.enrich.realisation;

import com.uwetrottmann.tmdb2.Tmdb;
import com.uwetrottmann.tmdb2.entities.DiscoverFilter;
import com.uwetrottmann.tmdb2.entities.MovieResultsPage;
import com.uwetrottmann.tmdb2.enumerations.SortBy;
import com.uwetrottmann.tmdb2.services.DiscoverService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import retrofit2.Response;
import ru.mike.moviebot.dto.Movie;
import ru.mike.moviebot.mapper.MovieMapper;
import ru.mike.moviebot.service.movieservice.GenresTopMovieService;

import java.io.IOException;
import java.util.List;

@Component
public class GenresTopMovieEnricher {
    @Autowired
    private MovieMapper movieMapper;
    private Tmdb tmdb = new Tmdb(System.getenv("TMDB_TOKEN"));
    private DiscoverService discoverService = tmdb.discoverService();
    private Logger log = LogManager.getLogger(GenresTopMovieService.class.getName());

    public List<Movie> enrichGenresTopMoviesByGenre(int genre, Integer page) {
        Response<MovieResultsPage> response = null;

        try {
            response = discoverService
                    .discoverMovie(
                            "ru", "ru", SortBy.POPULARITY_DESC, null, null, null,
                            false, false, page, 0, null, null,
                            null, null, null, null, null, null,
                            null, null, null, new DiscoverFilter(genre),
                            null, null, null, null,
                            null, null, null, null, null).execute();
        } catch (IOException e) {
            log.log(Level.ERROR, e);
        }

        return movieMapper.convertToMovieList(response);
    }

    public List<Movie> enrichGenresTopMoviesByYear(Integer year, Integer page) {
        Response<MovieResultsPage> response = null;
        try {
            response = discoverService
                    .discoverMovie(
                            "ru", "ru", SortBy.POPULARITY_DESC,
                            null, null, null, false, false, page, year,
                            null, null, null, null, null, null,
                            null, null, null, null, null,
                            null, null, null, year, null,
                            null, null, null, null, null).execute();
        } catch (IOException e) {
            log.log(Level.ERROR, e);
        }

        return movieMapper.convertToMovieList(response);
    }

    public List<Movie> enrichGenresTopMoviesByRating(Integer page) {
        Response<MovieResultsPage> response = null;
        try {
            response = discoverService
                    .discoverMovie(
                            "ru", "ru", SortBy.POPULARITY_DESC, null, null, null, false,
                            false, page, null, null, null, null,
                            null, null, null, null, null, null,
                            null, null, null, null, null,
                            null, null, null, null, null, null,
                            null).execute();
        } catch (IOException e) {
            log.log(Level.ERROR, e);
        }

        return movieMapper.convertToMovieList(response);
    }


}
