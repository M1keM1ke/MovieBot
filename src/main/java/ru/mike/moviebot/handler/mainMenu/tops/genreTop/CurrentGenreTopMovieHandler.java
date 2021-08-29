package ru.mike.moviebot.handler.mainMenu.tops.genreTop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.mike.moviebot.BotApplication;
import ru.mike.moviebot.api.telegram.sending.TelegramSending;
import ru.mike.moviebot.domain.User;
import ru.mike.moviebot.domain.genre.BaseGenre;
import ru.mike.moviebot.domain.top.GenreTop;
import ru.mike.moviebot.dto.Movie;
import ru.mike.moviebot.handler.IButtonHandler;
import ru.mike.moviebot.keyboard.KeyboardUtils;
import ru.mike.moviebot.service.common.TelegramService;
import ru.mike.moviebot.service.database.BaseGenresService;
import ru.mike.moviebot.service.database.GenreTopService;
import ru.mike.moviebot.service.database.MovieListService;
import ru.mike.moviebot.service.database.UserService;
import ru.mike.moviebot.service.movieservice.GenresTopMovieService;
import ru.mike.moviebot.service.statemachineService.BotStateMachineService;
import ru.mike.moviebot.statemachine.event.BotEvent;
import ru.mike.moviebot.statemachine.state.BotState;

import java.util.List;

@Component
public class CurrentGenreTopMovieHandler implements TelegramSending, IButtonHandler {
    @Autowired
    private KeyboardUtils keyboardUtils;
    @Autowired
    private UserService userService;
    @Autowired
    private BotStateMachineService stateMachineService;
    @Autowired
    private BaseGenresService baseGenresService;
    @Autowired
    GenresTopMovieService genresTopMovieService;
    @Autowired
    GenreTopService genreTopService;
    @Autowired
    MovieListService movieListService;

    @Override
    public String getMessage(Update update, String userMessage) {
        if (!update.hasCallbackQuery()) {
            return "default";
        }
        User user = userService.findUserByChatId(TelegramService.getMessage(update).getChatId());
        return user.getUserState() == BotState.GENRE_TOP ? userMessage : "default";
    }

    @Override
    public void execute(BotApplication botApplication, Update update) {
        stateMachineService.reachState(BotState.CURRENT_GENRE_TOP,
                BotEvent.REACH_CURRENT_GENRE_TOP, TelegramService.getMessage(update).getChatId());

        movieListService.unselectAllMovieLists(TelegramService.getMessage(update).getChatId());

        GenreTop genreTop = genreTopService.enableGenreEntity(update);
        BaseGenre baseGenre = baseGenresService.findBaseGenreByName(update.getCallbackQuery().getData());
        List<Movie> movieList = genresTopMovieService
                .findGenresTopMoviesByGenre(baseGenre.getGenreValue(), genreTop.getPage());

        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage
                .setChatId(TelegramService.getMessage(update).getChatId())
                .setMessageId(TelegramService.getMessage(update).getMessageId());
        deleteBotMessage(botApplication, deleteMessage);

        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto
                .setChatId(TelegramService.getMessage(update).getChatId())
                .setPhoto(movieList.get(genreTop.getMovieNum()).getUrlPoster())
                .setCaption(movieList.get(genreTop.getMovieNum()).toString());

        User userByChatId = userService.findUserByChatId(TelegramService.getMessage(update).getChatId());
        if (userByChatId.getMovieList().isEmpty()) {
            sendPhoto.setReplyMarkup(keyboardUtils
                    .continueGenreKeyboard().setInlineContinueByGenreTopButtons());
        } else {
            sendPhoto.setReplyMarkup(keyboardUtils
                    .continueGenreKeyboard().setInlineContinueByGenreAndAddToListTopButtons());
        }
        sendBotPhoto(botApplication, sendPhoto);

        Movie currentMovie = movieList.get(genreTop.getMovieNum());
        userService.setCurrentMovieIdIntoUser(TelegramService.getMessage(update).getChatId(), currentMovie.getId());

        genreTop.setMovieNum(genreTop.getMovieNum() + 1);
        genreTopService.saveGenreTop(genreTop);
    }
}
