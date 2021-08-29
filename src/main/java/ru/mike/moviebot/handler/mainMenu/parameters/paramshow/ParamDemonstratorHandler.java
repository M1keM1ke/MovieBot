package ru.mike.moviebot.handler.mainMenu.parameters.paramshow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.mike.moviebot.BotApplication;
import ru.mike.moviebot.api.telegram.sending.TelegramSending;
import ru.mike.moviebot.domain.genre.BaseGenre;
import ru.mike.moviebot.domain.param.ParamMovie;
import ru.mike.moviebot.handler.IButtonHandler;
import ru.mike.moviebot.keyboard.KeyboardUtils;
import ru.mike.moviebot.service.common.TelegramService;
import ru.mike.moviebot.service.database.BaseGenresService;
import ru.mike.moviebot.service.database.ParamMovieService;
import ru.mike.moviebot.service.database.UserService;

@Component
public class ParamDemonstratorHandler implements TelegramSending, IButtonHandler {
    @Autowired
    ParamMovieService paramMovieService;
    @Autowired
    KeyboardUtils keyboardUtils;
    @Autowired
    UserService userService;
    @Autowired
    BaseGenresService baseGenresService;

    @Override
    public String getMessage() {
        return "eaaa091d-7263-4504-95bf-b5b8d2a27c2b";
    }

    @Override
    public void execute(BotApplication botApplication, Update update) {
        ParamMovie paramMovie = paramMovieService
                .findParamMovieByChatId(TelegramService.getMessage(update).getChatId());

        String genre = "";
        String year = "";
        String rating = "";
        String actor = "";

        if (paramMovie.getGenreId() != null) {
            BaseGenre baseGenre = baseGenresService.findBaseGenreById(paramMovie.getGenreId());
            genre = baseGenre.getGenreName();
        }

        if (paramMovie.getYear() != null) year = paramMovie.getYear().toString();
        if (paramMovie.getPopularity() != null) rating = paramMovie.getPopularity().toString();
        if (paramMovie.getActor() != null) actor = paramMovie.getActor();

        EditMessageText editMessageText = new EditMessageText();
        editMessageText
                .setChatId(TelegramService.getMessage(update).getChatId())
                .setMessageId(TelegramService.getMessage(update).getMessageId())
                .setText("Вы выбрали\n" +
                        "жанр: " + genre +
                        "\nгод: " + year +
                        "\nрейтинг: " + rating +
                        "\nактер: " + actor)
                .setReplyMarkup(keyboardUtils.commonKeyboards().setEmptyKeyboard());
        sendBotEditMessage(botApplication, editMessageText);
    }
}
