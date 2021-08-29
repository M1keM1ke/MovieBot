package ru.mike.moviebot.keyboard.mainMenu;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.mike.moviebot.config.property.CommandsPropertyConfig;

import java.util.ArrayList;
import java.util.List;

public class MainMenuKeyboard {
    @Autowired
    private CommandsPropertyConfig commandsBot;

    public ReplyKeyboardMarkup setStartMenuButtons() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        setupStartKeyboard(replyKeyboardMarkup);

        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add(new KeyboardButton(commandsBot.getMainMenu().getRandom_movie()));
        keyboardRow1.add(new KeyboardButton(commandsBot.getMainMenu().getTop_movie()));

        KeyboardRow keyboardRow2 = new KeyboardRow();
        keyboardRow2.add(new KeyboardButton(commandsBot.getMainMenu().getParam_movie()));
        keyboardRow2.add(new KeyboardButton(commandsBot.getMainMenu().getList_movie()));

        List<KeyboardRow> startMenuList = new ArrayList<>();
        replyKeyboardMarkup.setKeyboard(startMenuList);

        startMenuList.add(keyboardRow1);
        startMenuList.add(keyboardRow2);

        return replyKeyboardMarkup;
    }

    private void setupStartKeyboard(ReplyKeyboardMarkup replyKeyboardMarkup) {
        replyKeyboardMarkup
                .setSelective(true)
                .setResizeKeyboard(true)
                .setOneTimeKeyboard(false);
    }
}
