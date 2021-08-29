package ru.mike.moviebot.handler.mainMenu.parameters.filters.actorFilter;

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
public class ActorParamHandler implements TelegramSending, IButtonHandler {
    @Autowired
    private BotStateMachineService stateMachineService;
    @Autowired
    KeyboardUtils keyboardUtils;

    @Override
    public String getMessage() {
        return "0d109028-a65f-4a92-a053-664ed8026cd0";
    }


    @Override
    public void execute(BotApplication botApplication, Update update) {
        stateMachineService.reachState(BotState.ACTOR_PARAM,
                BotEvent.REACH_ACTOR_PARAM, TelegramService.getMessage(update).getChatId());

        EditMessageText editMessageText = new EditMessageText();
        editMessageText
                .setChatId(TelegramService.getMessage(update).getChatId())
                .setMessageId(TelegramService.getMessage(update).getMessageId())
                .setText("Введи актера")
                .setReplyMarkup(keyboardUtils.actorParamKeyboard().setInlineDropActorParamButtons());
        sendBotEditMessage(botApplication, editMessageText);
    }
}
