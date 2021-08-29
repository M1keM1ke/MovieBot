package ru.mike.moviebot.handler.function.addToListType;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.mike.moviebot.domain.User;
import ru.mike.moviebot.domain.list.MovieList;
import ru.mike.moviebot.service.common.TelegramService;
import ru.mike.moviebot.service.database.MovieListService;
import ru.mike.moviebot.service.database.UserService;

public abstract class AbstractMovieListChangeHandler {
    @Autowired
    UserService userService;
    @Autowired
    MovieListService movieListService;

    protected void addMovieToListAndSetSelect(Update update) {
        User userByChatId = userService.findUserByChatId(TelegramService.getMessage(update).getChatId());
        MovieList movieListByData = movieListService
                .findMovieListByListCallbackData(update.getCallbackQuery().getData());

        movieListService.addMovieToList(
                userByChatId.getSessionId(),
                movieListByData.getTmdbListId(),
                userByChatId.getCurrentMovieId()
        );

        movieListByData.setWasListSelected(true);
        movieListService.saveMovieList(movieListByData);
    }

    protected EditMessageCaption createEditMessageCaptionWithoutKeyboard(Update update) {
        EditMessageCaption editMessageCaption = new EditMessageCaption();
        editMessageCaption
                .setChatId(TelegramService.getMessage(update).getChatId().toString())
                .setMessageId(TelegramService.getMessage(update).getMessageId())
                .setCaption(TelegramService.getMessage(update).getCaption());
        return editMessageCaption;
    }
}
