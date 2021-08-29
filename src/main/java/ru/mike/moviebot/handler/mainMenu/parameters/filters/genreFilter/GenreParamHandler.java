package ru.mike.moviebot.handler.mainMenu.parameters.filters.genreFilter;

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
public class GenreParamHandler implements TelegramSending, IButtonHandler {
    @Autowired
    private KeyboardUtils keyboardUtils;
    @Autowired
    private BotStateMachineService stateMachineService;

    @Override
    public String getMessage(Update update, String userMessage) {
        return "c2d5164d-1c83-4d9a-adff-8a26202235b5";
    }

    @Override
    public void execute(BotApplication botApplication, Update update) {
        stateMachineService.reachState(BotState.GENRE_PARAM,
                BotEvent.REACH_GENRE_PARAM, TelegramService.getMessage(update).getChatId());

        EditMessageText editMessageText = new EditMessageText();
        editMessageText
                .setChatId(TelegramService.getMessage(update).getChatId())
                .setMessageId(TelegramService.getMessage(update).getMessageId())
                .setText("Выбери жанр")
                .setReplyMarkup(keyboardUtils.genreParamKeyboard().setParamGenreListInlineButtons());
        sendBotEditMessage(botApplication, editMessageText);
    }
}
