package ru.mike.moviebot.handler.mainMenu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.mike.moviebot.BotApplication;
import ru.mike.moviebot.api.telegram.sending.TelegramSending;
import ru.mike.moviebot.config.property.BotMessagePropertyConfig;
import ru.mike.moviebot.config.property.CommandsPropertyConfig;
import ru.mike.moviebot.handler.IButtonHandler;
import ru.mike.moviebot.keyboard.KeyboardUtils;
import ru.mike.moviebot.service.common.TelegramService;
import ru.mike.moviebot.service.database.UserService;

@Component
public class StartMenuHandler implements TelegramSending, IButtonHandler {
    @Autowired
    CommandsPropertyConfig commandsConf;
    @Autowired
    BotMessagePropertyConfig botConf;
    @Autowired
    UserService userService;
    @Autowired
    KeyboardUtils keyboardUtils;

    @Override
    public String getMessage() {
        return commandsConf.getCommonMenu().getStart();
    }

    @Override
    public void execute(BotApplication botApplication, Update update) {
        SendMessage sendMessage = new SendMessage();
        sendMessage
                .setChatId(TelegramService.getMessage(update).getChatId())
                .setReplyMarkup(keyboardUtils.mainMenuKeyboard().setStartMenuButtons());

        String userName = TelegramService.getMessage(update).getChat().getUserName();

        if (!userService.existsUserByChatId(TelegramService.getMessage(update).getChatId())) {
            userService.createNewUser(update);
            if (userName != null && !userName.isEmpty()) {
                sendMessage
                        .setText(String.format("%s, %s!", botConf.getGreetingMsg(), userName));
                sendBotMessage(botApplication, sendMessage);
            } else {
                sendMessage
                        .setText(botConf.getGreetingMsg());
                sendBotMessage(botApplication, sendMessage);
            }
        } else {
            sendMessage
                    .setText(String.format("И снова здравствуй, %s!", userName));
            sendBotMessage(botApplication, sendMessage);
        }
    }
}