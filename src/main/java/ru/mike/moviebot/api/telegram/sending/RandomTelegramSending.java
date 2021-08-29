package ru.mike.moviebot.api.telegram.sending;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.mike.moviebot.BotApplication;

public interface RandomTelegramSending extends TelegramSending {
    Logger log = LogManager.getLogger(RandomTelegramSending.class.getName());

    @Override
    default void sendBotPhoto(BotApplication botApplication, SendPhoto sendPhoto) {
        try {
            botApplication.execute(sendPhoto);
        } catch (TelegramApiException e) {
            log.log(Level.WARN, "Bad photo url at user-" + sendPhoto.getChatId());
            SendMessage sendMessage = new SendMessage();
            sendMessage
                    .setChatId(sendPhoto.getChatId())
                    .setText(sendPhoto.getCaption())
                    .setReplyMarkup(sendPhoto.getReplyMarkup());
            sendBotMessage(botApplication, sendMessage);
        }
    }
}
