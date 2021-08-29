package ru.mike.moviebot.keyboard.function.continueType;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.mike.moviebot.config.property.CommandsPropertyConfig;

public class ContinueParamMovieKeyboard extends ContinueKeyboard{
    @Autowired
    private CommandsPropertyConfig commandsBot;

    public InlineKeyboardMarkup setInlineContinueParamMovieButtons() {
        return setInlineContinueButtons(
                commandsBot.getContinueButton().getContinue_inline(),
                "e460d897-37cb-4767-9fa5-4d99268108a9"
        );
    }

    public InlineKeyboardMarkup setInlineContinueByParamAndAddToListTopButtons() {
        return setInlineContinueAndAddToListButtons(
                commandsBot.getContinueButton().getContinue_inline(),
                "e460d897-37cb-4767-9fa5-4d99268108a9"
        );
    }
}