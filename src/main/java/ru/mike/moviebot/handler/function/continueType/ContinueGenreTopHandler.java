package ru.mike.moviebot.handler.function.continueType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import ru.mike.moviebot.BotApplication;
import ru.mike.moviebot.api.telegram.sending.TelegramSending;
import ru.mike.moviebot.domain.top.GenreTop;
import ru.mike.moviebot.dto.Movie;
import ru.mike.moviebot.handler.IButtonHandler;
import ru.mike.moviebot.keyboard.KeyboardUtils;
import ru.mike.moviebot.service.common.TelegramService;
import ru.mike.moviebot.service.database.GenreTopService;
import ru.mike.moviebot.service.database.MovieListService;
import ru.mike.moviebot.service.database.UserService;
import ru.mike.moviebot.service.movieservice.GenresTopMovieService;
import ru.mike.moviebot.service.statemachineService.BotStateMachineService;
import ru.mike.moviebot.statemachine.event.BotEvent;
import ru.mike.moviebot.statemachine.state.BotState;

import java.util.List;

@Component
public class ContinueGenreTopHandler implements TelegramSending, IButtonHandler {
    @Autowired
    private KeyboardUtils keyboardUtils;
    @Autowired
    private GenreTopService genreTopService;
    @Autowired
    private BotStateMachineService stateMachineService;
    @Autowired
    private GenresTopMovieService genresTopMovieService;
    @Autowired
    private MovieListService movieListService;
    @Autowired
    private UserService userService;

    @Override
    public String getMessage() {
        return "25d0fea8-f562-418e-90ef-01dbd25ca06f";
    }

    @Override
    public void execute(BotApplication botApplication, Update update) {
        stateMachineService.reachState(BotState.CONTINUE_GENRE,
                BotEvent.REACH_CONTINUE_GENRE, TelegramService.getMessage(update).getChatId());

        movieListService.unselectAllMovieLists(TelegramService.getMessage(update).getChatId());

        GenreTop genreTop = genreTopService.findGenreTopByChatId(TelegramService.getMessage(update).getChatId());
        setContinueGenre(botApplication, update, genreTop);
    }

    @Override
    public void sendBotPhoto(BotApplication botApplication, SendPhoto sendPhoto) {
        try {
            botApplication.execute(sendPhoto);
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
            //слишком длинное описание
            sendPhoto.setCaption("Описание отсутствует");
            try {
                botApplication.execute(sendPhoto);
            } catch (TelegramApiException telegramApiException) {
                telegramApiException.printStackTrace();
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void setContinueGenre(BotApplication botApplication, Update update, GenreTop newGenreTop) {
        List<Movie> movieList = genresTopMovieService
                .findGenresTopMoviesByGenre(newGenreTop.getGenreId(), newGenreTop.getPage());

        if (newGenreTop.getMovieNum() == movieList.size() || movieList.isEmpty()) {
            newGenreTop.setMovieNum(0);
            newGenreTop.setPage(newGenreTop.getPage() + 1);
            movieList = genresTopMovieService
                    .findGenresTopMoviesByGenre(newGenreTop.getGenreId(), newGenreTop.getPage());
        }
        Integer currentMovieId = movieList.get(newGenreTop.getMovieNum()).getId();
        userService.setCurrentMovieIdIntoUser(TelegramService.getMessage(update).getChatId(), currentMovieId);

        //удаляем клавиатуру у текущего сообщения
        EditMessageCaption editMessageCaption = new EditMessageCaption();
        editMessageCaption
                .setChatId(TelegramService.getMessage(update).getChatId().toString())
                .setMessageId(TelegramService.getMessage(update).getMessageId())
                .setCaption(TelegramService.getMessage(update).getCaption())
                .setReplyMarkup(keyboardUtils.commonKeyboards().setEmptyKeyboard());
        sendBotEditCaption(botApplication, editMessageCaption);

        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto
                .setChatId(TelegramService.getMessage(update).getChatId())
                .setPhoto(movieList.get(newGenreTop.getMovieNum()).getUrlPoster())
                .setCaption(movieList.get(newGenreTop.getMovieNum()).toString());

        if (keyboardUtils.addingToListKeyboard().setInlineShowAvailableMovieListsButtons(update).isPresent()) {
            sendPhoto.setReplyMarkup(keyboardUtils
                    .continueRatingTopKeyboard().setInlineContinueByRatingAndAddToListTopButtons());
        } else {
            sendPhoto.setReplyMarkup(keyboardUtils
                    .continueRatingTopKeyboard().setInlineContinueByRatingTopButtons());
        }
        sendBotPhoto(botApplication, sendPhoto);

        Movie currentMovie = movieList.get(newGenreTop.getMovieNum());
        userService.setCurrentMovieIdIntoUser(TelegramService.getMessage(update).getChatId(), currentMovie.getId());

        newGenreTop.setMovieNum(newGenreTop.getMovieNum() + 1);
        genreTopService.saveGenreTop(newGenreTop);
    }
}