package ru.mike.moviebot.keyboard.function.continueType;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.mike.moviebot.config.property.CommandsPropertyConfig;

public class ContinueYearTopKeyboard extends ContinueKeyboard{
    @Autowired
    private CommandsPropertyConfig commandsBot;

    public InlineKeyboardMarkup setInlineContinueByYearTopButtons() {
        return setInlineContinueButtons(
                commandsBot.getContinueButton().getContinue_inline(),
                "78d32dc8-d445-448d-8a78-c294371f8043"
        );
    }

    public InlineKeyboardMarkup setInlineContinueByYearAndAddToListTopButtons() {
        return setInlineContinueAndAddToListButtons(
                commandsBot.getContinueButton().getContinue_inline(),
                "78d32dc8-d445-448d-8a78-c294371f8043"
        );
    }
}