package ru.mike.moviebot.keyboard.function.continueType;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.mike.moviebot.config.property.CommandsPropertyConfig;

public class ContinueRatingTopKeyboard extends ContinueKeyboard {
    @Autowired
    private CommandsPropertyConfig commandsBot;

    public InlineKeyboardMarkup setInlineContinueByRatingTopButtons() {
        return setInlineContinueButtons(
                commandsBot.getContinueButton().getContinue_inline(),
                "82def356-9166-429a-93ea-5c85caec25e2"
        );
    }

    public InlineKeyboardMarkup setInlineContinueByRatingAndAddToListTopButtons() {
        return setInlineContinueAndAddToListButtons(
                commandsBot.getContinueButton().getContinue_inline(),
                "82def356-9166-429a-93ea-5c85caec25e2"
        );
    }
}