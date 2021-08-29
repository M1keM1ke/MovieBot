package ru.mike.moviebot.keyboard.common;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public class CommonKeyboards {

    public InlineKeyboardMarkup setEmptyKeyboard() {
        return new InlineKeyboardMarkup();
    }

    public InlineKeyboardButton getAddToListButton() {
        InlineKeyboardButton inlineAddToMovieListButton = new InlineKeyboardButton();
        inlineAddToMovieListButton.setText("Добавить в список");
        inlineAddToMovieListButton.setCallbackData("597a8769-8fdd-42de-8d88-3f2a8c3e45ed");
        return inlineAddToMovieListButton;
    }
}
