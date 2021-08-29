package ru.mike.moviebot.handler.mainMenu.lists.currentList.functions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.mike.moviebot.BotApplication;
import ru.mike.moviebot.api.telegram.sending.TelegramSending;
import ru.mike.moviebot.domain.User;
import ru.mike.moviebot.domain.list.MovieList;
import ru.mike.moviebot.dto.MovieOfListDto;
import ru.mike.moviebot.handler.IButtonHandler;
import ru.mike.moviebot.keyboard.KeyboardUtils;
import ru.mike.moviebot.service.common.TelegramService;
import ru.mike.moviebot.service.database.MovieListService;
import ru.mike.moviebot.service.database.UserService;

import java.util.List;

@Component
public class ShowingMoviesOfListHandler implements TelegramSending, IButtonHandler {
    @Autowired
    UserService userService;
    @Autowired
    MovieListService movieListService;
    @Autowired
    KeyboardUtils keyboardUtils;

    @Override
    public String getMessage() {
        return "a007fa9e-e17c-4d58-841a-7e27d4a8d231";
    }

    @Override
    public void execute(BotApplication botApplication, Update update) {
        User userByChatId = userService.findUserByChatId(TelegramService.getMessage(update).getChatId());
        MovieList movieListByData = movieListService
                .findMovieListByListCallbackData(userByChatId.getCurrentChosenMovieList());

        List<MovieOfListDto> movies = movieListService.getMoviesOfList(movieListByData.getTmdbListId());

        if (movies != null && movies.isEmpty()) {
            EditMessageText editMessageText = new EditMessageText();
            editMessageText
                    .setMessageId(TelegramService.getMessage(update).getMessageId())
                    .setChatId(TelegramService.getMessage(update).getChatId())
                    .setText("Список пуст")
                    .setReplyMarkup(keyboardUtils.currentMovieListKeyboard().setInlineCurrentMovieListButtons());
            sendBotEditMessage(botApplication, editMessageText);
        } else {
            StringBuilder listOfMovies = parseListToString(movies);

            SendMessage sendMessage = new SendMessage();
            sendMessage
                    .setChatId(TelegramService.getMessage(update).getChatId())
                    .setText(listOfMovies.toString());
            sendBotMessage(botApplication, sendMessage);
        }
    }

    /**
     * @param movies список фильмов
     * @return список фильмов в виде строки для отображения пользователю
     */
    private StringBuilder parseListToString(List<MovieOfListDto> movies) {
        StringBuilder listOfMovies = new StringBuilder();
        movies
                .forEach(movieOfListDto -> {
                    listOfMovies.append(movieOfListDto.toString()).append("\n").append("●").append("\n");
                });
        return listOfMovies;
    }
}
