package ru.mike.moviebot.handler.mainMenu.lists;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.mike.moviebot.BotApplication;
import ru.mike.moviebot.api.telegram.sending.TelegramSending;
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
public class MovieListsHandler implements TelegramSending, IButtonHandler {
    @Autowired
    CommandsPropertyConfig commandsConf;
    @Autowired
    KeyboardUtils keyboardUtils;
    @Autowired
    BotStateMachineService stateMachineService;
    @Autowired
    GenreTopService genreTopService;
    @Autowired
    YearTopService yearTopService;
    @Autowired
    RatingTopService ratingTopService;
    @Autowired
    ParamMovieService paramMovieService;
    @Autowired
    MovieListService movieListService;

    @Override
    public String getMessage() {
        return commandsConf.getMainMenu().getList_movie();
    }

    @Override
    public void execute(BotApplication botApplication, Update update) {
        stateMachineService.reachState(BotState.LIST,
                BotEvent.REACH_LIST, TelegramService.getMessage(update).getChatId());

        genreTopService.disableGenreEntity(update);
        yearTopService.disableYearEntity(update);
        ratingTopService.disableRatingEntity(update);
        paramMovieService.disableParamEntity(update);
        movieListService.unselectAllMovieLists(TelegramService.getMessage(update).getChatId());

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(TelegramService.getMessage(update).getChatId());
        sendMessage.setText("Мои списки");
        sendMessage.setReplyMarkup(keyboardUtils.movieListsKeyboard().setInlineListKeyboard(update));

        sendBotMessage(botApplication, sendMessage);
    }
}
