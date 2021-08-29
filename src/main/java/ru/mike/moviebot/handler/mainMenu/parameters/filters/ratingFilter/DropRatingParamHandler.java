package ru.mike.moviebot.handler.mainMenu.parameters.filters.ratingFilter;

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
public class DropRatingParamHandler implements TelegramSending, IButtonHandler {
    @Autowired
    KeyboardUtils keyboardUtils;
    @Autowired
    ParamMovieService paramMovieService;
    @Autowired
    BotStateMachineService stateMachineService;

    @Override
    public String getMessage() {
        return "5dd38f47-98d8-4ae6-ad0d-702e6c0b16f5";
    }

    @Override
    public void execute(BotApplication botApplication, Update update) {
        stateMachineService.reachState(BotState.DROP_RATING_PARAM,
                BotEvent.REACH_DROP_RATING_PARAM, TelegramService.getMessage(update).getChatId());

        ParamMovie paramMovie = paramMovieService
                .findParamMovieByChatId(TelegramService.getMessage(update).getChatId());
        paramMovie.setPopularity(null);
        paramMovieService.saveParamMovie(paramMovie);

        EditMessageText editMessageText = new EditMessageText();
        editMessageText
                .setChatId(TelegramService.getMessage(update).getChatId())
                .setMessageId(TelegramService.getMessage(update).getMessageId())
                .setText("Вы сбросили рейтинг")
                .setReplyMarkup(keyboardUtils.paramMovieKeyboard().setInlineCustomMoviesButtons());
        sendBotEditMessage(botApplication, editMessageText);
    }
}
