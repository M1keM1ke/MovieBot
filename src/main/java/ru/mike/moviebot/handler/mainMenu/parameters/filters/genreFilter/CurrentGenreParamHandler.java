package ru.mike.moviebot.handler.mainMenu.parameters.filters.genreFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.mike.moviebot.BotApplication;
import ru.mike.moviebot.api.telegram.sending.TelegramSending;
import ru.mike.moviebot.domain.User;
import ru.mike.moviebot.domain.genre.BaseGenre;
import ru.mike.moviebot.domain.param.ParamMovie;
import ru.mike.moviebot.handler.IButtonHandler;
import ru.mike.moviebot.keyboard.KeyboardUtils;
import ru.mike.moviebot.service.common.TelegramService;
import ru.mike.moviebot.service.database.BaseGenresService;
import ru.mike.moviebot.service.database.ParamMovieService;
import ru.mike.moviebot.service.database.UserService;
import ru.mike.moviebot.service.statemachineService.BotStateMachineService;
import ru.mike.moviebot.statemachine.event.BotEvent;
import ru.mike.moviebot.statemachine.state.BotState;

@Component
public class CurrentGenreParamHandler implements TelegramSending, IButtonHandler {
    @Autowired
    private KeyboardUtils keyboardUtils;
    @Autowired
    private ParamMovieService paramMovieService;
    @Autowired
    private UserService userService;
    @Autowired
    private BotStateMachineService stateMachineService;
    @Autowired
    private BaseGenresService baseGenresService;

    /**
     * @return userMessage if CurrentGenreParamHandler doesn't intersect with DropGenreParamHandler
     * because they have identical BotState
     */
    @Override
    public String getMessage(Update update, String userMessage) {
        if (!update.hasCallbackQuery()) {
            return "default";
        }
        User user = userService.findUserByChatId(TelegramService.getMessage(update).getChatId());
        return user.getUserState() == BotState.GENRE_PARAM  &&
                !update.getCallbackQuery().getData().equals("57a5aeeb-003b-46ca-ac7f-7552b0185df0")
                ? userMessage : "default";
    }

    @Override
    public void execute(BotApplication botApplication, Update update) {
        stateMachineService.reachState(BotState.CURRENT_GENRE_PARAM,
                BotEvent.REACH_CURRENT_GENRE_PARAM, TelegramService.getMessage(update).getChatId());

        User user = userService.findUserByChatId(TelegramService.getMessage(update).getChatId());
        ParamMovie paramMovie = paramMovieService.findParamMovieByChatId(user.getChatId());
        BaseGenre baseGenre = baseGenresService.findBaseGenreByName(update.getCallbackQuery().getData());

        EditMessageText editMessageText = new EditMessageText();
        editMessageText
                .setChatId(TelegramService.getMessage(update).getChatId())
                .setMessageId(TelegramService.getMessage(update).getMessageId())
                .setText("Вы ввели жанр: " + baseGenre.getGenreName())
                .setReplyMarkup(keyboardUtils.paramMovieKeyboard().setInlineCustomMoviesButtons());
        sendBotEditMessage(botApplication, editMessageText);

        paramMovie.setGenreId(baseGenre.getGenreValue());
        paramMovieService.saveParamMovie(paramMovie);
    }
}
