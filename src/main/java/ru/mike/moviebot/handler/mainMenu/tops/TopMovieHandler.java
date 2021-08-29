package ru.mike.moviebot.handler.mainMenu.tops;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.mike.moviebot.BotApplication;
import ru.mike.moviebot.api.telegram.sending.TelegramSending;
import ru.mike.moviebot.config.property.BotMessagePropertyConfig;
import ru.mike.moviebot.config.property.CommandsPropertyConfig;
import ru.mike.moviebot.handler.IButtonHandler;
import ru.mike.moviebot.keyboard.KeyboardUtils;
import ru.mike.moviebot.service.common.TelegramService;
import ru.mike.moviebot.service.database.GenreTopService;
import ru.mike.moviebot.service.database.MovieListService;
import ru.mike.moviebot.service.database.ParamMovieService;
import ru.mike.moviebot.service.database.RatingTopService;
import ru.mike.moviebot.service.database.YearTopService;
import ru.mike.moviebot.service.statemachineService.BotStateMachineService;
import ru.mike.moviebot.statemachine.event.BotEvent;
import ru.mike.moviebot.statemachine.state.BotState;

@Component
public class TopMovieHandler implements TelegramSending, IButtonHandler {
    @Autowired
    private KeyboardUtils keyboardUtils;
    @Autowired
    private BotMessagePropertyConfig botConf;
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
    MovieListService movieListService;

    @Override
    public String getMessage() {
        return commandsConf.getMainMenu().getTop_movie();
    }

    @Override
    public void execute(BotApplication botApplication, Update update) {
        genreTopService.disableGenreEntity(update);
        yearTopService.disableYearEntity(update);
        ratingTopService.disableRatingEntity(update);
        paramMovieService.disableParamEntity(update);
        movieListService.unselectAllMovieLists(TelegramService.getMessage(update).getChatId());

        stateMachineService.reachState(BotState.TOP, BotEvent.REACH_TOP,
                                    TelegramService.getMessage(update).getChatId());

        SendMessage sendMessage = new SendMessage();
        sendMessage
                .setChatId(TelegramService.getMessage(update).getChatId())
                .setText(botConf.getTopMovieMsg())
                .setReplyMarkup(keyboardUtils.topsKeyboard().setInlineMovieTopsButtons());
        sendBotMessage(botApplication, sendMessage);
    }
}
