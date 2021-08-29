package ru.mike.moviebot.handler.mainMenu.parameters.filters.ratingFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.mike.moviebot.BotApplication;
import ru.mike.moviebot.api.telegram.sending.TelegramSending;
import ru.mike.moviebot.handler.IButtonHandler;
import ru.mike.moviebot.keyboard.KeyboardUtils;
import ru.mike.moviebot.service.common.TelegramService;
import ru.mike.moviebot.service.statemachineService.BotStateMachineService;
import ru.mike.moviebot.statemachine.event.BotEvent;
import ru.mike.moviebot.statemachine.state.BotState;

@Component
public class RatingParamHandler implements TelegramSending, IButtonHandler {
    @Autowired
    private BotStateMachineService stateMachineService;
    @Autowired
    KeyboardUtils keyboardUtils;

    @Override
    public String getMessage() {
        return "8092a842-0b31-4479-a5f7-6ff175503076";
    }

    @Override
    public void execute(BotApplication botApplication, Update update) {
        stateMachineService.reachState(BotState.RATING_PARAM,
                BotEvent.REACH_RATING_PARAM, TelegramService.getMessage(update).getChatId());

        EditMessageText editMessageText = new EditMessageText();
        editMessageText
                .setChatId(TelegramService.getMessage(update).getChatId())
                .setMessageId(TelegramService.getMessage(update).getMessageId())
                .setText("Введи рейтинг, ниже которого не хочешь получить фильмы")
                .setReplyMarkup(keyboardUtils.ratingParamKeyboard().setInlineDropRatingParamButtons());
        sendBotEditMessage(botApplication, editMessageText);
    }
}
