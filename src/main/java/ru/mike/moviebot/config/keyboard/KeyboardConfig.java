package ru.mike.moviebot.config.keyboard;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.mike.moviebot.keyboard.common.CommonKeyboards;
import ru.mike.moviebot.keyboard.function.addToListType.AddingToMovieListKeyboard;
import ru.mike.moviebot.keyboard.function.continueType.ContinueGenreTopKeyboard;
import ru.mike.moviebot.keyboard.function.continueType.ContinueParamMovieKeyboard;
import ru.mike.moviebot.keyboard.function.continueType.ContinueRatingTopKeyboard;
import ru.mike.moviebot.keyboard.function.continueType.ContinueYearTopKeyboard;
import ru.mike.moviebot.keyboard.mainMenu.MainMenuKeyboard;
import ru.mike.moviebot.keyboard.mainMenu.lists.MovieListsKeyboard;
import ru.mike.moviebot.keyboard.mainMenu.lists.currentList.CurrentMovieListKeyboard;
import ru.mike.moviebot.keyboard.mainMenu.parameters.ParamMovieKeyboard;
import ru.mike.moviebot.keyboard.mainMenu.parameters.actorFilter.ActorParamKeyboard;
import ru.mike.moviebot.keyboard.mainMenu.parameters.genreFilter.GenreParamKeyboard;
import ru.mike.moviebot.keyboard.mainMenu.parameters.ratingFilter.RatingParamKeyboard;
import ru.mike.moviebot.keyboard.mainMenu.parameters.yearFilter.YearParamKeyboard;
import ru.mike.moviebot.keyboard.mainMenu.random.RandomMovieKeyboard;
import ru.mike.moviebot.keyboard.mainMenu.tops.TopsKeyboard;
import ru.mike.moviebot.keyboard.mainMenu.tops.genreTop.GenreTopKeyboard;
import ru.mike.moviebot.keyboard.mainMenu.tops.ratingTop.RatingTopKeyboard;
import ru.mike.moviebot.keyboard.mainMenu.tops.yearTop.YearTopKeyboard;

@Configuration
public class KeyboardConfig {

    @Bean
    public MainMenuKeyboard mainMenuKeyboard() {
        return new MainMenuKeyboard();
    }

    @Bean
    public RandomMovieKeyboard randomMovieKeyboard() {
        return new RandomMovieKeyboard();
    }

    @Bean
    public TopsKeyboard topsKeyboard() {
        return new TopsKeyboard();
    }

    @Bean
    GenreTopKeyboard genreTopKeyboard() {
        return new GenreTopKeyboard();
    }

    @Bean
    RatingTopKeyboard ratingTopKeyboard() {
        return new RatingTopKeyboard();
    }

    @Bean
    YearTopKeyboard yearTopKeyboard() {
        return new YearTopKeyboard();
    }

    @Bean
    ParamMovieKeyboard paramMovieKeyboard() {
        return new ParamMovieKeyboard();
    }

    @Bean
    ActorParamKeyboard actorParamKeyboard() {
        return new ActorParamKeyboard();
    }

    @Bean
    GenreParamKeyboard genreParamKeyboard() {
        return new GenreParamKeyboard();
    }

    @Bean
    RatingParamKeyboard ratingParamKeyboard() {
        return new RatingParamKeyboard();
    }

    @Bean
    YearParamKeyboard yearParamKeyboard() {
        return new YearParamKeyboard();
    }

    @Bean
    ContinueGenreTopKeyboard continueGenreKeyboard() {
        return new ContinueGenreTopKeyboard();
    }

    @Bean
    CommonKeyboards emptyKeyboard() {
        return new CommonKeyboards();
    }

    @Bean
    ContinueParamMovieKeyboard continueParamMovieKeyboard() {
        return new ContinueParamMovieKeyboard();
    }

    @Bean
    ContinueYearTopKeyboard continueYearTopKeyboard() {
        return new ContinueYearTopKeyboard();
    }

    @Bean
    ContinueRatingTopKeyboard continueRatingTopKeyboard() {
        return new ContinueRatingTopKeyboard();
    }

    @Bean
    MovieListsKeyboard movieListsKeyboard() {
        return new MovieListsKeyboard();
    }

    @Bean
    CurrentMovieListKeyboard currentMovieListKeyboard() {
        return new CurrentMovieListKeyboard();
    }

    @Bean
    AddingToMovieListKeyboard addingToListKeyboard() {
        return new AddingToMovieListKeyboard();
    }
}
