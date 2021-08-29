package ru.mike.moviebot.keyboard.function.continueType;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.mike.moviebot.config.property.CommandsPropertyConfig;

public class ContinueGenreTopKeyboard extends ContinueKeyboard {
    @Autowired
    private CommandsPropertyConfig commandsBot;

    public InlineKeyboardMarkup setInlineContinueByGenreTopButtons() {
        return setInlineContinueButtons(
                commandsBot.getContinueButton().getContinue_inline(),
                "25d0fea8-f562-418e-90ef-01dbd25ca06f"
        );
    }

    public InlineKeyboardMarkup setInlineContinueByGenreAndAddToListTopButtons() {
        return setInlineContinueAndAddToListButtons(
                commandsBot.getContinueButton().getContinue_inline(),
                "25d0fea8-f562-418e-90ef-01dbd25ca06f"
        );
    }
}