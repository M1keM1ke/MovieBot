package ru.mike.moviebot.handler.mainMenu.lists.currentList.functions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.mike.moviebot.BotApplication;
import ru.mike.moviebot.api.telegram.sending.TelegramSending;
import ru.mike.moviebot.domain.User;
import ru.mike.moviebot.domain.list.MovieList;
import ru.mike.moviebot.handler.IButtonHandler;
import ru.mike.moviebot.keyboard.KeyboardUtils;
import ru.mike.moviebot.service.common.TelegramService;
import ru.mike.moviebot.service.database.MovieListService;
import ru.mike.moviebot.service.database.UserService;

@Component
public class ClearingMovieListHandler implements TelegramSending, IButtonHandler {
    @Autowired
    UserService userService;
    @Autowired
    MovieListService movieListService;
    @Autowired
    KeyboardUtils keyboardUtils;

    @Override
    public String getMessage() {
        return "77eb9e5d-8286-40d0-b5a6-0abbb7fcfe51";
    }

    @Override
    public void execute(BotApplication botApplication, Update update) {
        Long chatId = TelegramService.getMessage(update).getChatId();

        User userByChatId = userService.findUserByChatId(chatId);
        MovieList movieListByListCallbackData = movieListService
                .findMovieListByListCallbackData(userByChatId.getCurrentChosenMovieList());

        movieListService.clearMovieListById(userByChatId.getSessionId(), movieListByListCallbackData.getTmdbListId());

        EditMessageText editMessageText = new EditMessageText();
        editMessageText
                .setMessageId(TelegramService.getMessage(update).getMessageId())
                .setChatId(TelegramService.getMessage(update).getChatId())
                .setText("Список очищен")
                .setReplyMarkup(keyboardUtils.currentMovieListKeyboard().setInlineCurrentMovieListButtons());
        sendBotEditMessage(botApplication, editMessageText);

    }
}
