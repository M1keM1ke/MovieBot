package ru.mike.moviebot.keyboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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

@Component
public class KeyboardUtils {
    @Autowired
    private MainMenuKeyboard mainMenuKeyboard;
    @Autowired
    private RandomMovieKeyboard randomMovieKeyboard;
    @Autowired
    private TopsKeyboard topsKeyboard;
    @Autowired
    private GenreTopKeyboard genreTopKeyboard;
    @Autowired
    private RatingTopKeyboard ratingTopKeyboard;
    @Autowired
    private YearTopKeyboard yearTopKeyboard;
    @Autowired
    private ParamMovieKeyboard paramMovieKeyboard;
    @Autowired
    private ActorParamKeyboard actorParamKeyboard;
    @Autowired
    private GenreParamKeyboard genreParamKeyboard;
    @Autowired
    private RatingParamKeyboard ratingParamKeyboard;
    @Autowired
    private YearParamKeyboard yearParamKeyboard;
    @Autowired
    private ContinueGenreTopKeyboard continueGenreTopKeyboard;
    @Autowired
    private CommonKeyboards commonKeyboards;
    @Autowired
    private ContinueParamMovieKeyboard continueParamMovieKeyboard;
    @Autowired
    private ContinueYearTopKeyboard continueYearTopKeyboard;
    @Autowired
    private ContinueRatingTopKeyboard continueRatingTopKeyboard;
    @Autowired
    private MovieListsKeyboard movieListsKeyboard;
    @Autowired
    private CurrentMovieListKeyboard currentMovieListKeyboard;
    @Autowired
    private AddingToMovieListKeyboard addingToMovieListKeyboard;

    public MainMenuKeyboard mainMenuKeyboard() {
        return mainMenuKeyboard;
    }

    public RandomMovieKeyboard randomMovieKeyboard() {
        return randomMovieKeyboard;
    }

    public TopsKeyboard topsKeyboard() {
        return topsKeyboard;
    }

    public GenreTopKeyboard genreTopKeyboard() {
        return genreTopKeyboard;
    }

    public RatingTopKeyboard ratingTopKeyboard() {
        return ratingTopKeyboard;
    }

    public YearTopKeyboard yearTopKeyboard() {
        return yearTopKeyboard;
    }

    public ParamMovieKeyboard paramMovieKeyboard() {
        return paramMovieKeyboard;
    }

    public ActorParamKeyboard actorParamKeyboard() {
        return actorParamKeyboard;
    }

    public GenreParamKeyboard genreParamKeyboard() {
        return genreParamKeyboard;
    }

    public RatingParamKeyboard ratingParamKeyboard() {
        return ratingParamKeyboard;
    }

    public YearParamKeyboard yearParamKeyboard() {
        return yearParamKeyboard;
    }

    public ContinueGenreTopKeyboard continueGenreKeyboard() {
        return continueGenreTopKeyboard;
    }

    public CommonKeyboards commonKeyboards() {
        return commonKeyboards;
    }

    public ContinueParamMovieKeyboard continueParamMovieKeyboard() {
        return continueParamMovieKeyboard;
    }

    public ContinueYearTopKeyboard continueYearTopKeyboard() {
        return continueYearTopKeyboard;
    }

    public ContinueRatingTopKeyboard continueRatingTopKeyboard() {
        return continueRatingTopKeyboard;
    }

    public MovieListsKeyboard movieListsKeyboard() {
        return movieListsKeyboard;
    }

    public CurrentMovieListKeyboard currentMovieListKeyboard() {
        return currentMovieListKeyboard;
    }

    public AddingToMovieListKeyboard addingToListKeyboard() {
        return addingToMovieListKeyboard;
    }
}
