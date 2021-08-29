package ru.mike.moviebot.handler.mainMenu.parameters.filters.yearFilter;

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
public class YearParamHandler implements TelegramSending, IButtonHandler {
    @Autowired
    private BotStateMachineService stateMachineService;
    @Autowired
    KeyboardUtils keyboardUtils;

    @Override
    public String getMessage() {
        return "291eeea2-1e6e-49bd-aa8b-ed7c25e9f366";
    }

    @Override
    public void execute(BotApplication botApplication, Update update) {
        stateMachineService.reachState(BotState.YEAR_PARAM,
                BotEvent.REACH_YEAR_PARAM, TelegramService.getMessage(update).getChatId());

        EditMessageText editMessageText = new EditMessageText();
        editMessageText
                .setChatId(TelegramService.getMessage(update).getChatId())
                .setMessageId(TelegramService.getMessage(update).getMessageId())
                .setText("Просто введи год, по которому хочешь получить фильм")
                .setReplyMarkup(keyboardUtils.yearParamKeyboard().setInlineDropYearParamButtons());
        sendBotEditMessage(botApplication, editMessageText);
    }
}
