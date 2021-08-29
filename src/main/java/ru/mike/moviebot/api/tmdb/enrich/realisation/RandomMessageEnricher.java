package ru.mike.moviebot.api.tmdb.enrich.realisation;

import com.uwetrottmann.tmdb2.Tmdb;
import com.uwetrottmann.tmdb2.entities.BaseMovie;
import com.uwetrottmann.tmdb2.entities.MovieResultsPage;
import com.uwetrottmann.tmdb2.services.SearchService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import retrofit2.Response;
import ru.mike.moviebot.dto.Movie;
import ru.mike.moviebot.mapper.MovieMapper;
import ru.mike.moviebot.service.movieservice.RandomMovieService;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class RandomMessageEnricher {
    @Autowired
    private MovieMapper movieMapper;
    private Tmdb tmdb = new Tmdb(System.getenv("TMDB_TOKEN"));
    private SearchService searchService = tmdb.searchService();
    private boolean hasTitle;
    private Response<MovieResultsPage> response;
    private Movie movie;
    private Logger log = LogManager.getLogger(RandomMovieService.class.getName());

    public Movie enrichRandomMovie() {
        hasTitle = false;
        try {
            while (!hasTitle) {
                String ruLetter = "йцукенгшщзхъфывапролджэячсмитьбю";

                String generatedQuery = Character.toString(
                        ruLetter.charAt(ThreadLocalRandom.current().nextInt(0, 32)));
                Integer generatedPage = ThreadLocalRandom.current().nextInt(1, 10);
                Integer generatedYear = ThreadLocalRandom.current().nextInt(1990, 2021);

                response = searchService
                        .movie(generatedQuery, generatedPage, "ru",
                                "ru", false, generatedYear, null)
                        .execute();

                if (response.isSuccessful() && response.body().results.size() > 0) {
                    BaseMovie movie = response.body().results.get(0);
                    this.movie = movieMapper.toMovie(movie);
                    hasTitle = true;
                }
            }
        } catch (IOException ioException) {
            log.log(Level.ERROR, ioException);
        }
        return movie;
    }
}
