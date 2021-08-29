package ru.mike.moviebot.handler.mainMenu.randomFilms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.mike.moviebot.BotApplication;
import ru.mike.moviebot.api.telegram.sending.RandomTelegramSending;
import ru.mike.moviebot.config.property.CommandsPropertyConfig;
import ru.mike.moviebot.domain.User;
import ru.mike.moviebot.dto.Movie;
import ru.mike.moviebot.handler.IButtonHandler;
import ru.mike.moviebot.keyboard.KeyboardUtils;
import ru.mike.moviebot.service.common.TelegramService;
import ru.mike.moviebot.service.database.GenreTopService;
import ru.mike.moviebot.service.database.MovieListService;
import ru.mike.moviebot.service.database.ParamMovieService;
import ru.mike.moviebot.service.database.RatingTopService;
import ru.mike.moviebot.service.database.UserService;
import ru.mike.moviebot.service.database.YearTopService;
import ru.mike.moviebot.service.movieservice.RandomMovieService;
import ru.mike.moviebot.service.statemachineService.BotStateMachineService;
import ru.mike.moviebot.statemachine.event.BotEvent;
import ru.mike.moviebot.statemachine.state.BotState;

@Component
public class RandomMovieHandler implements RandomTelegramSending, IButtonHandler {
    @Autowired
    private RandomMovieService randomMovieService;
    @Autowired
    private CommandsPropertyConfig commandsConf;
    @Autowired
    private BotStateMachineService stateMachineService;
    @Autowired
    GenreTopService genreTopService;
    @Autowired
    RatingTopService ratingTopService;
    @Autowired
    YearTopService yearTopService;
    @Autowired
    ParamMovieService paramMovieService;
    @Autowired
    UserService userService;
    @Autowired
    KeyboardUtils keyboardUtils;
    @Autowired
    MovieListService movieListService;

    @Override
    public String getMessage() {
        return commandsConf.getMainMenu().getRandom_movie();
    }

    @Override
    public void execute(BotApplication botApplication, Update update) {
        genreTopService.disableGenreEntity(update);
        yearTopService.disableYearEntity(update);
        ratingTopService.disableRatingEntity(update);
        paramMovieService.disableParamEntity(update);
        movieListService.unselectAllMovieLists(TelegramService.getMessage(update).getChatId());

        stateMachineService.reachState(BotState.RANDOM, BotEvent.REACH_RANDOM,
                TelegramService.getMessage(update).getChatId());

        Movie movie = randomMovieService.getRandomMovie();

        SendPhoto sendPhoto = new SendPhoto().setChatId(TelegramService.getMessage(update).getChatId())
                .setCaption(movie.toString())
                .setPhoto(movie.getUrlPoster());

        //если у юзера нет списков, то выводим пустую клавиатуру
        User userByChatId = userService.findUserByChatId(TelegramService.getMessage(update).getChatId());
        if (userByChatId.getMovieList().isEmpty()) {
            sendPhoto.setReplyMarkup(keyboardUtils.commonKeyboards().setEmptyKeyboard());
        } else {
            sendPhoto.setReplyMarkup(keyboardUtils.randomMovieKeyboard().setInlineRandomMovieButtons());
        }

        sendBotPhoto(botApplication, sendPhoto);

        userService.setCurrentMovieIdIntoUser(TelegramService.getMessage(update).getChatId(), movie.getId());
    }
}
