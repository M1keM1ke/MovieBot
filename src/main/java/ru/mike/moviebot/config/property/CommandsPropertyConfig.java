package ru.mike.moviebot.config.property;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "buttons")
@Getter
public class CommandsPropertyConfig {
    CommonMenu commonMenu = new CommonMenu();
    MainMenuCommand mainMenu = new MainMenuCommand();
    TopMovieCommand topMovieMenu = new TopMovieCommand();
    ParamMovieCommand paramMovie = new ParamMovieCommand();
    Genre genre = new Genre();
    Continue continueButton = new Continue();

    @Data
    public static class CommonMenu {
        String start;
        String main_menu;
    }

    @Data
    public  static class MainMenuCommand {
        String random_movie;
        String top_movie;
        String param_movie;
        String list_movie;
    }

    @Data
    public static class TopMovieCommand {
        String by_genre;
        String by_year;
        String by_rating;
    }

    @Data
    public static class ParamMovieCommand {
        String year;
        String genre;
        String rating;
    }

    @Data
    public static class Genre {
        String action;
        String Adventure;
        String animation;
        String comedy;
        String crime;
        String documentary;
        String drama;
        String family;
        String fantasy;
        String history;
        String horror;
        String music;
        String mystery;
        String romance;
        String science_fiction;
        String tv_movie;
        String thriller;
        String war;
        String western;

        Integer actionId;
        Integer adventureId;
        Integer animationId;
        Integer comedyId;
        Integer crimeId;
        Integer documentaryId;
        Integer dramaId;
        Integer familyId;
        Integer fantasyId;
        Integer historyId;
        Integer horrorId;
        Integer musicId;
        Integer mysteryId;
        Integer romanceId;
        Integer science_fictionId;
        Integer tv_movieId;
        Integer thrillerId;
        Integer warId;
        Integer westernId;
    }
    @Data
    public static class Continue {
        String continue_inline;
    }
}
