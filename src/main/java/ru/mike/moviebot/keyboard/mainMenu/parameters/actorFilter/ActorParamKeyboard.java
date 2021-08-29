package ru.mike.moviebot.keyboard.mainMenu.parameters.actorFilter;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class ActorParamKeyboard {

    public InlineKeyboardMarkup setInlineDropActorParamButtons() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton inlineDropActorParamButton = new InlineKeyboardButton();
        inlineDropActorParamButton.setText("Сбросить параметр");
        inlineDropActorParamButton.setCallbackData("8c47cc2a-d862-4f40-83f1-9bd80669ecc3");

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(inlineDropActorParamButton);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);

        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }
}
