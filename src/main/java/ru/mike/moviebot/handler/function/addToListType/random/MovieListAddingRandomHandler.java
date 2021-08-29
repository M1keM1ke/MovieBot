package ru.mike.moviebot.handler.function.addToListType.random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.mike.moviebot.BotApplication;
import ru.mike.moviebot.api.telegram.sending.TelegramSending;
import ru.mike.moviebot.domain.User;
import ru.mike.moviebot.domain.list.MovieList;
import ru.mike.moviebot.handler.IButtonHandler;
import ru.mike.moviebot.handler.function.addToListType.AbstractMovieListChangeHandler;
import ru.mike.moviebot.keyboard.KeyboardUtils;
import ru.mike.moviebot.service.common.TelegramService;
import ru.mike.moviebot.service.database.MovieListService;
import ru.mike.moviebot.service.database.UserService;
import ru.mike.moviebot.statemachine.state.BotState;

import java.util.Optional;

@Component
public class MovieListAddingRandomHandler
        extends AbstractMovieListChangeHandler implements TelegramSending, IButtonHandler {
    @Autowired
    UserService userService;
    @Autowired
    MovieListService movieListService;
    @Autowired
    KeyboardUtils keyboardUtils;

    @Override
    public String getMessage(Update update, String userMessage) {
        if (!update.hasCallbackQuery()) {
            return "default";
        }
        User userByChatId = userService.findUserByChatId(TelegramService.getMessage(update).getChatId());
        Optional<MovieList> list = movieListService
                .findMovieListByListCallbackDataForHandlerCheck(update.getCallbackQuery().getData());

        if (list.isPresent() && userByChatId.getUserState() == BotState.RANDOM) {
            return list.get().getListCallbackData();
        }
        return "default";
    }

    @Override
    public void execute(BotApplication botApplication, Update update) {
        addMovieToListAndSetSelect(update);
        Optional<InlineKeyboardMarkup> inlineKeyboardMarkup = keyboardUtils.addingToListKeyboard().setInlineShowAvailableMovieListsButtons(update);

        /*
         * некоторые фильмы могут быть без постера, поэтому проверяем, есть ли к нему описание, если его нет -
         * постера тоже нет. Тогда шлем EditMessageText, иначе с EditMessageCaption будет exception
         * при нажатии на список
         */
        if (TelegramService.getMessage(update).getCaption() == null) {
            EditMessageText editMessageText = new EditMessageText();
            editMessageText
                    .setChatId(TelegramService.getMessage(update).getChatId().toString())
                    .setMessageId(TelegramService.getMessage(update).getMessageId())
                    .setText(TelegramService.getMessage(update).getText());

            if (inlineKeyboardMarkup.isPresent()) {
                editMessageText.setReplyMarkup(inlineKeyboardMarkup.get());
            } else {
                editMessageText.setReplyMarkup(keyboardUtils.commonKeyboards().setEmptyKeyboard());
            }
            sendBotEditMessage(botApplication, editMessageText);

        } else {
            EditMessageCaption editMessageCaption = createEditMessageCaptionWithoutKeyboard(update);
            if (inlineKeyboardMarkup.isPresent()) {
                editMessageCaption.setReplyMarkup(keyboardUtils.randomMovieKeyboard().setInlineRandomMovieButtons());
            } else {
                editMessageCaption.setReplyMarkup(keyboardUtils.commonKeyboards().setEmptyKeyboard());
            }
            sendBotEditCaption(botApplication, editMessageCaption);

        }
    }
}
