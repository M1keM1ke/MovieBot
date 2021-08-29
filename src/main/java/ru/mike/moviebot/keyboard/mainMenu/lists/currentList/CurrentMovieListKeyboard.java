package ru.mike.moviebot.keyboard.mainMenu.lists.currentList;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class CurrentMovieListKeyboard {

    public InlineKeyboardMarkup setInlineCurrentMovieListButtons() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton inlineDeleteMovieListButton = new InlineKeyboardButton();
        inlineDeleteMovieListButton.setText("Удалить список");
        inlineDeleteMovieListButton.setCallbackData("d24bff4c-e3ef-46e9-8d0d-5920550f814c");

        InlineKeyboardButton inlineClearMovieListButton = new InlineKeyboardButton();
        inlineClearMovieListButton.setText("Очистить список");
        inlineClearMovieListButton.setCallbackData("77eb9e5d-8286-40d0-b5a6-0abbb7fcfe51");

        InlineKeyboardButton inlineShowMoviesOfListButton = new InlineKeyboardButton();
        inlineShowMoviesOfListButton.setText("Показать фильмы");
        inlineShowMoviesOfListButton.setCallbackData("a007fa9e-e17c-4d58-841a-7e27d4a8d231");

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(inlineDeleteMovieListButton);
        keyboardButtonsRow1.add(inlineClearMovieListButton);

        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        keyboardButtonsRow2.add(inlineShowMoviesOfListButton);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);

        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }
}
