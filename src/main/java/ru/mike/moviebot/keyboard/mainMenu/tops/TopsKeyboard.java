package ru.mike.moviebot.keyboard.mainMenu.tops;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.mike.moviebot.config.property.CommandsPropertyConfig;

import java.util.ArrayList;
import java.util.List;

public class TopsKeyboard {
    @Autowired
    private CommandsPropertyConfig commandsBot;

    public InlineKeyboardMarkup setInlineMovieTopsButtons() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton inlineByGenreButton = new InlineKeyboardButton();
        inlineByGenreButton.setText(commandsBot.getTopMovieMenu().getBy_genre());
        inlineByGenreButton.setCallbackData("5dedb409-51d5-40be-8919-eae860bfb2b7");

        InlineKeyboardButton inlineByYearButton = new InlineKeyboardButton();
        inlineByYearButton.setText(commandsBot.getTopMovieMenu().getBy_year());
        inlineByYearButton.setCallbackData("07fb9de7-33fe-4c8d-b5c0-9d6e40f0bfc4");

        InlineKeyboardButton inlineByRatingButton = new InlineKeyboardButton();
        inlineByRatingButton.setText(commandsBot.getTopMovieMenu().getBy_rating());
        inlineByRatingButton.setCallbackData("2f9c70ca-a8ce-417d-83bf-ad3772788938");

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(inlineByGenreButton);

        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        keyboardButtonsRow2.add(inlineByYearButton);

        List<InlineKeyboardButton> keyboardButtonsRow3 = new ArrayList<>();
        keyboardButtonsRow3.add(inlineByRatingButton);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);
        rowList.add(keyboardButtonsRow3);

        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }
}
