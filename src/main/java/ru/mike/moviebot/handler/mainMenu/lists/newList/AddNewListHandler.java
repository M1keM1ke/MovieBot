package ru.mike.moviebot.handler.mainMenu.lists.newList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.mike.moviebot.BotApplication;
import ru.mike.moviebot.api.telegram.sending.TelegramSending;
import ru.mike.moviebot.handler.IButtonHandler;
import ru.mike.moviebot.service.common.TelegramService;
import ru.mike.moviebot.service.statemachineService.BotStateMachineService;
import ru.mike.moviebot.statemachine.event.BotEvent;
import ru.mike.moviebot.statemachine.state.BotState;

@Component
public class AddNewListHandler implements TelegramSending, IButtonHandler {
    @Autowired
    BotStateMachineService stateMachineService;

    @Override
    public String getMessage() {
        return "999ab013-79e5-4231-8cd7-f1941ccf7dd2";
    }

    @Override
    public void execute(BotApplication botApplication, Update update) {
        stateMachineService.reachState(BotState.NEW_LIST,
                BotEvent.REACH_NEW_LIST, TelegramService.getMessage(update).getChatId());

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(TelegramService.getMessage(update).getChatId());
        sendMessage.setText("Введи имя списка");

        sendBotMessage(botApplication, sendMessage);
    }
}
