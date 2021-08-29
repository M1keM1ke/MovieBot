package ru.mike.moviebot.handler;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.mike.moviebot.BotApplication;

public interface IButtonHandler {
    Logger log = LogManager.getLogger(IButtonHandler.class.getName());

    default String getMessage(Update update, String userMessage) {
        return "noMessage";
    }

    default String getMessage() {
        return "default";
    }

    void execute(BotApplication botApplication, Update update);

}
