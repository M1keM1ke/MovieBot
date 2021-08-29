package ru.mike.moviebot.keyboard.mainMenu.parameters;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.mike.moviebot.config.property.CommandsPropertyConfig;

import java.util.ArrayList;
import java.util.List;

public class ParamMovieKeyboard {
    @Autowired
    private CommandsPropertyConfig commandsBot;

    public InlineKeyboardMarkup setInlineCustomMoviesButtons() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton inlineYearButton = new InlineKeyboardButton();
        inlineYearButton.setText(commandsBot.getParamMovie().getYear());
        inlineYearButton.setCallbackData("291eeea2-1e6e-49bd-aa8b-ed7c25e9f366");

        InlineKeyboardButton inlineGenreButton = new InlineKeyboardButton();
        inlineGenreButton.setText(commandsBot.getParamMovie().getGenre());
        inlineGenreButton.setCallbackData("c2d5164d-1c83-4d9a-adff-8a26202235b5");

        InlineKeyboardButton inlineRatingButton = new InlineKeyboardButton();
        inlineRatingButton.setText(commandsBot.getParamMovie().getRating());
        inlineRatingButton.setCallbackData("8092a842-0b31-4479-a5f7-6ff175503076");

        InlineKeyboardButton inlineActorButton = new InlineKeyboardButton();
        inlineActorButton.setText("актер");
        inlineActorButton.setCallbackData("0d109028-a65f-4a92-a053-664ed8026cd0");

        InlineKeyboardButton inlineFindMovieButton = new InlineKeyboardButton();
        inlineFindMovieButton.setText("найти фильмы");
        inlineFindMovieButton.setCallbackData("45dbda00-5b85-48be-bb3d-c21fec28f2be");

        InlineKeyboardButton inlineResetParametersButton = new InlineKeyboardButton();
        inlineResetParametersButton.setText("посмотреть параметры");
        inlineResetParametersButton.setCallbackData("eaaa091d-7263-4504-95bf-b5b8d2a27c2b");

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(inlineGenreButton);
        keyboardButtonsRow1.add(inlineYearButton);

        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        keyboardButtonsRow2.add(inlineRatingButton);
        keyboardButtonsRow2.add(inlineActorButton);

        List<InlineKeyboardButton> keyboardButtonsRow3 = new ArrayList<>();
        keyboardButtonsRow3.add(inlineFindMovieButton);
        keyboardButtonsRow3.add(inlineResetParametersButton);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);
        rowList.add(keyboardButtonsRow3);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }
}
