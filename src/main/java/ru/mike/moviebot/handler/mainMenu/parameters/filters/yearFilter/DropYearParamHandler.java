package ru.mike.moviebot.handler.mainMenu.parameters.filters.yearFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.mike.moviebot.BotApplication;
import ru.mike.moviebot.api.telegram.sending.TelegramSending;
import ru.mike.moviebot.domain.param.ParamMovie;
import ru.mike.moviebot.handler.IButtonHandler;
import ru.mike.moviebot.keyboard.KeyboardUtils;
import ru.mike.moviebot.service.common.TelegramService;
import ru.mike.moviebot.service.database.ParamMovieService;
import ru.mike.moviebot.service.statemachineService.BotStateMachineService;
import ru.mike.moviebot.statemachine.event.BotEvent;
import ru.mike.moviebot.statemachine.state.BotState;

@Component
public class DropYearParamHandler implements TelegramSending, IButtonHandler {
    @Autowired
    BotStateMachineService stateMachineService;
    @Autowired
    ParamMovieService paramMovieService;
    @Autowired
    KeyboardUtils keyboardUtils;

    @Override
    public String getMessage() {
        return "10d1405a-3e94-4773-a5a7-b60e0ceca2b9";
    }

    @Override
    public void execute(BotApplication botApplication, Update update) {
        stateMachineService.reachState(BotState.DROP_YEAR_PARAM,
                BotEvent.REACH_DROP_YEAR_PARAM, TelegramService.getMessage(update).getChatId());

        ParamMovie paramMovie = paramMovieService
                .findParamMovieByChatId(TelegramService.getMessage(update).getChatId());
        paramMovie.setYear(null);
        paramMovieService.saveParamMovie(paramMovie);

        EditMessageText editMessageText = new EditMessageText();
        editMessageText
                .setChatId(TelegramService.getMessage(update).getChatId())
                .setMessageId(TelegramService.getMessage(update).getMessageId())
                .setText("Вы сбросили год")
                .setReplyMarkup(keyboardUtils.paramMovieKeyboard().setInlineCustomMoviesButtons());
        sendBotEditMessage(botApplication, editMessageText);
    }
}
