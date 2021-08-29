package ru.mike.moviebot.service.common;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

public class TelegramService {

    public static Message getMessage(Update update) {
        if (update.hasCallbackQuery()) {
            return update.getCallbackQuery().getMessage();
        } else {
            return update.getMessage();
        }
    }
}
