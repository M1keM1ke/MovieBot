package ru.mike.moviebot.keyboard.mainMenu.random;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.mike.moviebot.keyboard.common.CommonKeyboards;

import java.util.ArrayList;
import java.util.List;

public class RandomMovieKeyboard {
    @Autowired
    CommonKeyboards commonKeyboards;

    public InlineKeyboardMarkup setInlineRandomMovieButtons() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton inlineAddToMovieListButton = commonKeyboards.getAddToListButton();

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(inlineAddToMovieListButton);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);

        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }
}
