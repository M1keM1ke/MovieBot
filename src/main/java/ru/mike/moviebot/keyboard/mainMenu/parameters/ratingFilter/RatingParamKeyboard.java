package ru.mike.moviebot.keyboard.mainMenu.parameters.ratingFilter;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class RatingParamKeyboard {

    public InlineKeyboardMarkup setInlineDropRatingParamButtons() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton inlineDropRatingParamButton = new InlineKeyboardButton();
        inlineDropRatingParamButton.setText("Сбросить параметр");
        inlineDropRatingParamButton.setCallbackData("5dd38f47-98d8-4ae6-ad0d-702e6c0b16f5");

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(inlineDropRatingParamButton);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }
}
