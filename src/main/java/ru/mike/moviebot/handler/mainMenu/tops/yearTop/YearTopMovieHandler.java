package ru.mike.moviebot.handler.mainMenu.tops.yearTop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.mike.moviebot.BotApplication;
import ru.mike.moviebot.api.telegram.sending.TelegramSending;
import ru.mike.moviebot.handler.IButtonHandler;
import ru.mike.moviebot.keyboard.KeyboardUtils;
import ru.mike.moviebot.service.common.TelegramService;
import ru.mike.moviebot.service.database.YearTopService;
import ru.mike.moviebot.service.statemachineService.BotStateMachineService;
import ru.mike.moviebot.statemachine.event.BotEvent;
import ru.mike.moviebot.statemachine.state.BotState;

@Component
public class YearTopMovieHandler implements TelegramSending, IButtonHandler {
    @Autowired
    private BotStateMachineService stateMachineService;
    @Autowired
    KeyboardUtils keyboardUtils;
    @Autowired
    YearTopService yearTopService;

    @Override
    public String getMessage() {
        return "07fb9de7-33fe-4c8d-b5c0-9d6e40f0bfc4";
    }

    @Override
    public void execute(BotApplication botApplication, Update update) {
        stateMachineService.reachState(BotState.YEAR_TOP,
                BotEvent.REACH_YEAR_TOP, TelegramService.getMessage(update).getChatId());

        EditMessageText editMessageText = new EditMessageText();
        editMessageText
                .setChatId(TelegramService.getMessage(update).getChatId())
                .setMessageId(TelegramService.getMessage(update).getMessageId())
                .setText("Просто введи год, по которому хочешь получить топ")
                .setReplyMarkup(keyboardUtils.commonKeyboards().setEmptyKeyboard());
        sendBotEditMessage(botApplication, editMessageText);
    }
}
