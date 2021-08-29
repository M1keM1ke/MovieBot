package ru.mike.moviebot.handler.function.addToListType.param;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
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
public class MovieListAddingParamHandler
        extends AbstractMovieListChangeHandler implements TelegramSending, IButtonHandler {
    @Autowired
    private UserService userService;
    @Autowired
    private MovieListService movieListService;
    @Autowired
    private KeyboardUtils keyboardUtils;

    @Override
    public String getMessage(Update update, String userMessage) {
        if (!update.hasCallbackQuery()) {
            return "default";
        }
        User userByChatId = userService.findUserByChatId(TelegramService.getMessage(update).getChatId());
        Optional<MovieList> list = movieListService
                .findMovieListByListCallbackDataForHandlerCheck(update.getCallbackQuery().getData());

        if (list.isPresent() && (userByChatId.getUserState() == BotState.FIND_PARAM_MOVIE ||
                userByChatId.getUserState() == BotState.CONTINUE_PARAM)) {
            return list.get().getListCallbackData();
        }
        return "default";
    }

    @Override
    public void execute(BotApplication botApplication, Update update) {
        addMovieToListAndSetSelect(update);
        Optional<InlineKeyboardMarkup> inlineKeyboardMarkup = keyboardUtils.addingToListKeyboard().setInlineShowAvailableMovieListsButtons(update);
        EditMessageCaption editMessageCaption = createEditMessageCaptionWithoutKeyboard(update);

        if (!inlineKeyboardMarkup.isPresent()) {
            editMessageCaption.setReplyMarkup(keyboardUtils
                    .continueParamMovieKeyboard().setInlineContinueParamMovieButtons());
        } else {
            InlineKeyboardMarkup keyboard = keyboardUtils
                    .continueParamMovieKeyboard().setInlineContinueByParamAndAddToListTopButtons();
            editMessageCaption.setReplyMarkup(keyboard);
        }
        sendBotEditCaption(botApplication, editMessageCaption);
    }
}
