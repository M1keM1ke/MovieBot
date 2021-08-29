package ru.mike.moviebot.keyboard.mainMenu.lists;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.mike.moviebot.domain.User;
import ru.mike.moviebot.domain.list.MovieList;
import ru.mike.moviebot.service.common.TelegramService;
import ru.mike.moviebot.service.database.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MovieListsKeyboard {
    @Autowired
    UserService userService;

    public InlineKeyboardMarkup setInlineListKeyboard(Update update) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton inlineNewListButton = new InlineKeyboardButton();
        inlineNewListButton.setText("Добавить новый список");
        inlineNewListButton.setCallbackData("999ab013-79e5-4231-8cd7-f1941ccf7dd2");

        User userByChatId = userService.findUserByChatId(TelegramService.getMessage(update).getChatId());
        Set<MovieList> movieList = userByChatId.getMovieList();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();

        movieList.forEach(list -> {
            InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
            inlineKeyboardButton.setText(list.getName());
            inlineKeyboardButton.setCallbackData(list.getListCallbackData());
            keyboardButtonsRow1.add(inlineKeyboardButton);
        });

        keyboardButtonsRow1.add(inlineNewListButton);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);

        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }


}
