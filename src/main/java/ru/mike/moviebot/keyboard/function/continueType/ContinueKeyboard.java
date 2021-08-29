package ru.mike.moviebot.keyboard.function.continueType;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.mike.moviebot.config.property.CommandsPropertyConfig;
import ru.mike.moviebot.keyboard.common.CommonKeyboards;

import java.util.ArrayList;
import java.util.List;

public class ContinueKeyboard {
    @Autowired
    private CommandsPropertyConfig commandsBot;
    @Autowired
    CommonKeyboards commonKeyboards;

    protected InlineKeyboardMarkup setInlineContinueAndAddToListButtons(String buttonText, String callbackData) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton inlineAddToListButton = commonKeyboards.getAddToListButton();

        List<InlineKeyboardButton> keyboardButtonsRow1 = getContinueButtonWithList(buttonText, callbackData);

        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        keyboardButtonsRow2.add(inlineAddToListButton);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup setInlineContinueButtons(String buttonText, String callbackData) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<InlineKeyboardButton> keyboardButtonsRow1 = getContinueButtonWithList(buttonText, callbackData);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    private List<InlineKeyboardButton> getContinueButtonWithList(String buttonText, String callbackData) {
        InlineKeyboardButton inlineContinueParamMovieButton = new InlineKeyboardButton();
        inlineContinueParamMovieButton.setText(buttonText);
        inlineContinueParamMovieButton.setCallbackData(callbackData);

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(inlineContinueParamMovieButton);
        return keyboardButtonsRow1;
    }
}
