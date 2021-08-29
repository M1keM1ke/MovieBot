package ru.mike.moviebot.keyboard.mainMenu.parameters.yearFilter;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class YearParamKeyboard {

    public InlineKeyboardMarkup setInlineDropYearParamButtons() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton inlineDropYearParamButton = new InlineKeyboardButton();
        inlineDropYearParamButton.setText("Сбросить параметр");
        inlineDropYearParamButton.setCallbackData("10d1405a-3e94-4773-a5a7-b60e0ceca2b9");

        List<InlineKeyboardButton> keboardButtonRow1 = new ArrayList<>();
        keboardButtonRow1.add(inlineDropYearParamButton);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keboardButtonRow1);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }
}
