package ru.mike.moviebot.api.telegram.sending;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import ru.mike.moviebot.BotApplication;

public interface TelegramSending {
    Logger logger = LoggerFactory.getLogger(TelegramSending.class);
    String MSG_NOT_MODIFIED = "message is not modified: specified new message content and reply markup are exactly " +
            "the same as a current content and reply markup of the message";

    default void sendBotPhoto(BotApplication botApplication, SendPhoto sendPhoto) {
        try {
            botApplication.execute(sendPhoto);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    default void sendBotEditMessage(BotApplication botApplication, EditMessageText editMessageText) {
        try {
            botApplication.execute(editMessageText);
        } catch (TelegramApiRequestException e) {
            logger.warn(MSG_NOT_MODIFIED);

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    default void sendBotEditCaption(BotApplication botApplication, EditMessageCaption editMessageCaption) {
        try {
            botApplication.execute(editMessageCaption);

        } catch (TelegramApiRequestException e) {
            logger.warn(MSG_NOT_MODIFIED);

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    default void sendBotMessage(BotApplication botApplication, SendMessage sendMessage) {
        try {
            botApplication.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    default void deleteBotMessage(BotApplication botApplication, DeleteMessage deleteMessage) {
        try {
            botApplication.execute(deleteMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}