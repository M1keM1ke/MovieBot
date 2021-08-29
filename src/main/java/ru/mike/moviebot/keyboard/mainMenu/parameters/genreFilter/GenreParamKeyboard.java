package ru.mike.moviebot.keyboard.mainMenu.parameters.genreFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.mike.moviebot.keyboard.mainMenu.tops.genreTop.GenreTopKeyboard;

import java.util.ArrayList;
import java.util.List;

public class GenreParamKeyboard {
    @Autowired
    GenreTopKeyboard genreTopKeyboard;

    public InlineKeyboardMarkup setParamGenreListInlineButtons() {
        InlineKeyboardMarkup inlineKeyboardMarkup = genreTopKeyboard.setGenreListInlineButtons();

        InlineKeyboardButton inlineDropGenreParamButton = new InlineKeyboardButton();
        inlineDropGenreParamButton.setText("Сбросить параметр");
        inlineDropGenreParamButton.setCallbackData("57a5aeeb-003b-46ca-ac7f-7552b0185df0");

        List<InlineKeyboardButton> keyboardButtonsrowLast = new ArrayList<>();
        keyboardButtonsrowLast.add(inlineDropGenreParamButton);

        inlineKeyboardMarkup.getKeyboard().add(keyboardButtonsrowLast);
        return inlineKeyboardMarkup;
    }
}
