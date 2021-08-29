package ru.mike.moviebot.handler.mainMenu.lists.newList;

import com.uwetrottmann.tmdb2.entities.ListCreateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
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
import ru.mike.moviebot.service.handler.HandlerService;
import ru.mike.moviebot.service.statemachineService.BotStateMachineService;
import ru.mike.moviebot.statemachine.event.BotEvent;
import ru.mike.moviebot.statemachine.state.BotState;

@Component
public class AddMovieListNameHandler implements TelegramSending, IButtonHandler {
    @Autowired
    UserService userService;
    @Autowired
    HandlerService handlerService;
    @Autowired
    BotStateMachineService stateMachineService;
    @Autowired
    MovieListService movieListService;
    @Autowired
    KeyboardUtils keyboardUtils;

    @Override
    public String getMessage(Update update, String userMessage) {
        if (update.hasCallbackQuery()) {
            return "default";
        }
        User userByChatId = userService.findUserByChatId(TelegramService.getMessage(update).getChatId());
        return userByChatId.getUserState() == BotState.NEW_LIST &&
                !handlerService.getMainMenuButtonNames().contains(userMessage)
                ? userMessage : "default";
    }

    /**
     * Создает список фильмов с заданным именем, которое приходит в update
     * @param botApplication - бот
     * @param update - ответ пользователя на ввод имени нового списка
     */
    @Override
    public void execute(BotApplication botApplication, Update update) {
        stateMachineService.reachState(BotState.LIST,
                BotEvent.REACH_LIST, TelegramService.getMessage(update).getChatId());

        User userByChatId = userService.findUserByChatId(TelegramService.getMessage(update).getChatId());

        ListCreateRequest listRequest = movieListService
                .createListRequest(TelegramService.getMessage(update).getText(), "some desc", "ru");

        MovieList newMovieList = movieListService.createNewMovieList(update, userByChatId.getSessionId(), listRequest);
        movieListService.saveMovieList(newMovieList);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(TelegramService.getMessage(update).getChatId());
        sendMessage.setText("Мои списки");
        sendMessage.setReplyMarkup(keyboardUtils.movieListsKeyboard().setInlineListKeyboard(update));

        sendBotMessage(botApplication, sendMessage);
    }
}
