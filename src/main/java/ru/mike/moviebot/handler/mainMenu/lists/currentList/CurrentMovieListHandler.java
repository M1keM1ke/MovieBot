package ru.mike.moviebot.handler.mainMenu.lists.currentList;

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
import ru.mike.moviebot.service.statemachineService.BotStateMachineService;
import ru.mike.moviebot.statemachine.state.BotState;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CurrentMovieListHandler implements TelegramSending, IButtonHandler {
    @Autowired
    UserService userService;
    @Autowired
    KeyboardUtils keyboardUtils;
    @Autowired
    MovieListService movieListService;
    @Autowired
    BotStateMachineService stateMachineService;

    /**
     * Проверяет uuid нажатого списка на наличие такого списка в базе данных
     * @param update - ответ пользователя на нажатие какого-то созданного списка
     * @param userMessage - сообщение пользователя, здесь всегда пустое
     * @return uuid списка, если он найден в базе данных, иначе "default"
     */
    @Override
    public String getMessage(Update update, String userMessage) {
        User userByChatId = userService.findUserByChatId(TelegramService.getMessage(update).getChatId());

        if (update.hasCallbackQuery() && userByChatId.getUserState() == BotState.LIST) {
            String movieListData = update.getCallbackQuery().getData();

            List<MovieList> movieLists = userByChatId.getMovieList().stream()
                    .filter((movieList -> movieList.getListCallbackData().equals(movieListData)))
                    .collect(Collectors.toList());

            if (movieLists.size() == 1) {
                return movieLists.get(0).getListCallbackData();
            }
        }
        return "default";
    }

    @Override
    public void execute(BotApplication botApplication, Update update) {
        User userByChatId = userService.findUserByChatId(TelegramService.getMessage(update).getChatId());
        userByChatId.setCurrentChosenMovieList(update.getCallbackQuery().getData());
        userService.saveUser(userByChatId);

        MovieList movieListByListCallbackData = movieListService
                .findMovieListByListCallbackData(update.getCallbackQuery().getData());

        String movieListName = movieListByListCallbackData.getName();

        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setMessageId(TelegramService.getMessage(update).getMessageId());
        editMessageText.setChatId(TelegramService.getMessage(update).getChatId());
        editMessageText.setText("Список фильмов: " + movieListName);
        editMessageText.setReplyMarkup(keyboardUtils.currentMovieListKeyboard().setInlineCurrentMovieListButtons());

        sendBotEditMessage(botApplication, editMessageText);
    }
}
