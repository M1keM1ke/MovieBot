package ru.mike.moviebot.keyboard.mainMenu.tops.genreTop;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.mike.moviebot.config.property.CommandsPropertyConfig;

import java.util.ArrayList;
import java.util.List;

public class GenreTopKeyboard {
    @Autowired
    private CommandsPropertyConfig commandsBot;

    public InlineKeyboardMarkup setGenreListInlineButtons() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton inlineActionButton = new InlineKeyboardButton();
        inlineActionButton.setText(commandsBot.getGenre().getAction());
        inlineActionButton.setCallbackData(commandsBot.getGenre().getAction());

        InlineKeyboardButton inlineAdventureButton = new InlineKeyboardButton();
        inlineAdventureButton.setText(commandsBot.getGenre().getAdventure());
        inlineAdventureButton.setCallbackData(commandsBot.getGenre().getAdventure());

        InlineKeyboardButton inlineAnimationButton = new InlineKeyboardButton();
        inlineAnimationButton.setText(commandsBot.getGenre().getAnimation());
        inlineAnimationButton.setCallbackData(commandsBot.getGenre().getAnimation());

        InlineKeyboardButton inlineComedyButton = new InlineKeyboardButton();
        inlineComedyButton.setText(commandsBot.getGenre().getComedy());
        inlineComedyButton.setCallbackData(commandsBot.getGenre().getComedy());

        InlineKeyboardButton inlineCrimeButton = new InlineKeyboardButton();
        inlineCrimeButton.setText(commandsBot.getGenre().getCrime());
        inlineCrimeButton.setCallbackData(commandsBot.getGenre().getCrime());

        InlineKeyboardButton inlineDocumentaryButton = new InlineKeyboardButton();
        inlineDocumentaryButton.setText(commandsBot.getGenre().getDocumentary());
        inlineDocumentaryButton.setCallbackData(commandsBot.getGenre().getDocumentary());

        InlineKeyboardButton inlineDramaButton = new InlineKeyboardButton();
        inlineDramaButton.setText(commandsBot.getGenre().getDrama());
        inlineDramaButton.setCallbackData(commandsBot.getGenre().getDrama());

        InlineKeyboardButton inlineFamilyButton = new InlineKeyboardButton();
        inlineFamilyButton.setText(commandsBot.getGenre().getFamily());
        inlineFamilyButton.setCallbackData(commandsBot.getGenre().getFamily());

        InlineKeyboardButton inlineFantasyButton = new InlineKeyboardButton();
        inlineFantasyButton.setText(commandsBot.getGenre().getFantasy());
        inlineFantasyButton.setCallbackData(commandsBot.getGenre().getFantasy());

        InlineKeyboardButton inlineHistoryButton = new InlineKeyboardButton();
        inlineHistoryButton.setText(commandsBot.getGenre().getHistory());
        inlineHistoryButton.setCallbackData(commandsBot.getGenre().getHistory());

        InlineKeyboardButton inlineHorrorButton = new InlineKeyboardButton();
        inlineHorrorButton.setText(commandsBot.getGenre().getHorror());
        inlineHorrorButton.setCallbackData(commandsBot.getGenre().getHorror());

        InlineKeyboardButton inlineMusicButton = new InlineKeyboardButton();
        inlineMusicButton.setText(commandsBot.getGenre().getMusic());
        inlineMusicButton.setCallbackData(commandsBot.getGenre().getMusic());

        InlineKeyboardButton inlineMysteryButton = new InlineKeyboardButton();
        inlineMysteryButton.setText(commandsBot.getGenre().getMystery());
        inlineMysteryButton.setCallbackData(commandsBot.getGenre().getMystery());

        InlineKeyboardButton inlineRomanceButton = new InlineKeyboardButton();
        inlineRomanceButton.setText(commandsBot.getGenre().getRomance());
        inlineRomanceButton.setCallbackData(commandsBot.getGenre().getRomance());

        InlineKeyboardButton inlineScienceFictionButton = new InlineKeyboardButton();
        inlineScienceFictionButton.setText(commandsBot.getGenre().getScience_fiction());
        inlineScienceFictionButton.setCallbackData(commandsBot.getGenre().getScience_fiction());

        InlineKeyboardButton inlineTvMovieButton = new InlineKeyboardButton();
        inlineTvMovieButton.setText(commandsBot.getGenre().getTv_movie());
        inlineTvMovieButton.setCallbackData(commandsBot.getGenre().getTv_movie());

        InlineKeyboardButton inlineThrillerButton = new InlineKeyboardButton();
        inlineThrillerButton.setText(commandsBot.getGenre().getThriller());
        inlineThrillerButton.setCallbackData(commandsBot.getGenre().getThriller());

        InlineKeyboardButton inlineWarButton = new InlineKeyboardButton();
        inlineWarButton.setText(commandsBot.getGenre().getWar());
        inlineWarButton.setCallbackData(commandsBot.getGenre().getWar());

        InlineKeyboardButton inlineWesternButton = new InlineKeyboardButton();
        inlineWesternButton.setText(commandsBot.getGenre().getWestern());
        inlineWesternButton.setCallbackData(commandsBot.getGenre().getWestern());


        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(inlineActionButton);
        keyboardButtonsRow1.add(inlineAdventureButton);

        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        keyboardButtonsRow2.add(inlineAnimationButton);
        keyboardButtonsRow2.add(inlineComedyButton);

        List<InlineKeyboardButton> keyboardButtonsRow3 = new ArrayList<>();
        keyboardButtonsRow3.add(inlineCrimeButton);
        keyboardButtonsRow3.add(inlineDocumentaryButton);

        List<InlineKeyboardButton> keyboardButtonsRow4 = new ArrayList<>();
        keyboardButtonsRow4.add(inlineDramaButton);
        keyboardButtonsRow4.add(inlineFamilyButton);

        List<InlineKeyboardButton> keyboardButtonsRow5 = new ArrayList<>();
        keyboardButtonsRow5.add(inlineFantasyButton);
        keyboardButtonsRow5.add(inlineHistoryButton);

        List<InlineKeyboardButton> keyboardButtonsRow6 = new ArrayList<>();
        keyboardButtonsRow6.add(inlineHorrorButton);
        keyboardButtonsRow6.add(inlineMusicButton);

        List<InlineKeyboardButton> keyboardButtonsRow7 = new ArrayList<>();
        keyboardButtonsRow7.add(inlineMysteryButton);
        keyboardButtonsRow7.add(inlineRomanceButton);

        List<InlineKeyboardButton> keyboardButtonsRow8 = new ArrayList<>();
        keyboardButtonsRow8.add(inlineScienceFictionButton);
        keyboardButtonsRow8.add(inlineTvMovieButton);

        List<InlineKeyboardButton> keyboardButtonsRow9 = new ArrayList<>();
        keyboardButtonsRow9.add(inlineThrillerButton);
        keyboardButtonsRow9.add(inlineWarButton);

        List<InlineKeyboardButton> keyboardButtonsRow10 = new ArrayList<>();
        keyboardButtonsRow10.add(inlineWesternButton);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);
        rowList.add(keyboardButtonsRow3);
        rowList.add(keyboardButtonsRow4);
        rowList.add(keyboardButtonsRow5);
        rowList.add(keyboardButtonsRow6);
        rowList.add(keyboardButtonsRow7);
        rowList.add(keyboardButtonsRow8);
        rowList.add(keyboardButtonsRow9);
        rowList.add(keyboardButtonsRow10);

        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }
}
