package ru.mike.moviebot.handler.mainMenu.lists.currentList.functions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.mike.moviebot.BotApplication;
import ru.mike.moviebot.api.telegram.sending.TelegramSending;
import ru.mike.moviebot.domain.User;
import ru.mike.moviebot.domain.list.MovieList;
import ru.mike.moviebot.handler.IButtonHandler;
import ru.mike.moviebot.keyboard.KeyboardUtils;
import ru.mike.moviebot.service.common.TelegramService;
import ru.mike.moviebot.service.database.MovieListService;
import ru.mike.moviebot.service.database.UserService;

import java.util.Optional;

@Component
public class DeletionMovieListHandler implements TelegramSending, IButtonHandler {
    @Autowired
    UserService userService;
    @Autowired
    MovieListService movieListService;
    @Autowired
    KeyboardUtils keyboardUtils;

    @Override
    public String getMessage() {
        return "d24bff4c-e3ef-46e9-8d0d-5920550f814c";
    }

    @Override
    public void execute(BotApplication botApplication, Update update) {
        User userByChatId = userService.findUserByChatId(TelegramService.getMessage(update).getChatId());

        MovieList movieListByData = movieListService
                .findMovieListByListCallbackData(userByChatId.getCurrentChosenMovieList());

        Boolean isDeleted = movieListService
                .deleteMovieListInDatabaseByListId(movieListByData.getTmdbListId());

        if (!isDeleted) {
            SendMessage sendMessage = new SendMessage();
            sendMessage
                    .setChatId(TelegramService.getMessage(update).getChatId())
                    .setText("Ой, что-то пошло не так, список нельзя удалить.");
            sendBotMessage(botApplication, sendMessage);
            return;
        }

        Optional<InlineKeyboardMarkup> inlineKeyboardMarkup = keyboardUtils
                .addingToListKeyboard().setInlineShowAvailableMovieListsButtons(update);

        EditMessageText editMessageText = new EditMessageText();
        editMessageText
                .setChatId(TelegramService.getMessage(update).getChatId())
                .setMessageId(TelegramService.getMessage(update).getMessageId())
                .setText("Список удален");

        if (inlineKeyboardMarkup.isPresent()) {
            editMessageText.setReplyMarkup(inlineKeyboardMarkup.get());
        } else {
            editMessageText.setReplyMarkup(keyboardUtils.commonKeyboards().setEmptyKeyboard());
        }

        sendBotEditMessage(botApplication, editMessageText);
    }
}
