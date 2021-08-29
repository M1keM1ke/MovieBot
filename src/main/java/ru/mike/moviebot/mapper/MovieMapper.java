package ru.mike.moviebot.mapper;

import com.uwetrottmann.tmdb2.entities.BaseMovie;
import com.uwetrottmann.tmdb2.entities.Media;
import com.uwetrottmann.tmdb2.entities.MovieResultsPage;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import retrofit2.Response;
import ru.mike.moviebot.dto.Movie;
import ru.mike.moviebot.dto.MovieOfListDto;

import java.util.ArrayList;
import java.util.List;

@Component
public class MovieMapper {
    private static final String POSTER_BASE_URL = "https://image.tmdb.org/t/p/w200";

    public List<Movie> convertToMovieList(Response<MovieResultsPage> response) {
        List<Movie> movieList = new ArrayList<>();

        if (response.isSuccessful()) {
            List<BaseMovie> topMoviesList = response.body().results;
            Movie movie;
            BaseMovie baseMovie;
            for (BaseMovie value : topMoviesList) {
                baseMovie = value;
                movie = toMovie(baseMovie);
                movieList.add(movie);
            }
        }
        return movieList;
    }

    public Movie toMovie(BaseMovie movie) {
        return new Movie(
                movie.id, movie.title,
                POSTER_BASE_URL + movie.poster_path,
                movie.overview,
                movie.popularity,
                movie.release_date);
    }

    public List<MovieOfListDto> convertToMovieOfListDtoList(@NonNull List<Media> items) {
        List<MovieOfListDto> movieList = new ArrayList<>();

        if (!items.isEmpty()) {
            MovieOfListDto movie;
            BaseMovie baseMovie;
            for (Media item : items) {
                baseMovie = item.movie;
                movie = toMovieOfListDto(baseMovie);
                movieList.add(movie);
            }
        }
        return movieList;

    }

    private MovieOfListDto toMovieOfListDto(BaseMovie movie) {
        return new MovieOfListDto(
                movie.id, movie.title,
                POSTER_BASE_URL + movie.poster_path,
                movie.overview,
                movie.popularity,
                movie.release_date);
    }
}
