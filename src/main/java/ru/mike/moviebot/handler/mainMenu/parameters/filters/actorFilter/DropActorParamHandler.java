package ru.mike.moviebot.handler.mainMenu.parameters.filters.actorFilter;

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
public class DropActorParamHandler implements TelegramSending, IButtonHandler {
    @Autowired
    ParamMovieService paramMovieService;
    @Autowired
    KeyboardUtils keyboardUtils;
    @Autowired
    BotStateMachineService stateMachineService;

    @Override
    public String getMessage() {
        return "8c47cc2a-d862-4f40-83f1-9bd80669ecc3";
    }

    @Override
    public void execute(BotApplication botApplication, Update update) {
        stateMachineService.reachState(BotState.DROP_ACTOR_PARAM,
                BotEvent.REACH_DROP_ACTOR_PARAM, TelegramService.getMessage(update).getChatId());

        ParamMovie paramMovie = paramMovieService
                .findParamMovieByChatId(TelegramService.getMessage(update).getChatId());

            paramMovieService.disableActor(paramMovie);

            EditMessageText editMessageText = new EditMessageText();
            editMessageText
                    .setChatId(TelegramService.getMessage(update).getChatId())
                    .setMessageId(TelegramService.getMessage(update).getMessageId())
                    .setText("Вы сбросили актера")
                    .setReplyMarkup(keyboardUtils.paramMovieKeyboard().setInlineCustomMoviesButtons());
            sendBotEditMessage(botApplication, editMessageText);
    }
}
