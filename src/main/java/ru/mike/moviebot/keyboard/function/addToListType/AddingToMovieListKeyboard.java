package ru.mike.moviebot.keyboard.function.addToListType;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.mike.moviebot.domain.list.MovieList;
import ru.mike.moviebot.service.common.TelegramService;
import ru.mike.moviebot.service.database.MovieListService;
import ru.mike.moviebot.service.database.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AddingToMovieListKeyboard {
    @Autowired
    UserService userService;
    @Autowired
    MovieListService movieListService;

    public Optional<InlineKeyboardMarkup> setInlineShowAvailableMovieListsButtons(Update update) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<MovieList> movieLists = movieListService.findMovieListsByChatIdandWasSelected(TelegramService.getMessage(update).getChatId(), false);

        if (movieLists.isEmpty()) {
            return Optional.empty();
        }

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        for(MovieList movieList : movieLists) {
            List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
            InlineKeyboardButton inlineAddToMovieListButton = new InlineKeyboardButton();
            inlineAddToMovieListButton.setText(movieList.getName());
            inlineAddToMovieListButton.setCallbackData(movieList.getListCallbackData());
            keyboardButtonsRow.add(inlineAddToMovieListButton);
            rowList.add(keyboardButtonsRow);
        }

        inlineKeyboardMarkup.setKeyboard(rowList);
        return Optional.of(inlineKeyboardMarkup);
    }
}
