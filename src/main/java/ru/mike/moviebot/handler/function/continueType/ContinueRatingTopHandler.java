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
import ru.mike.moviebot.domain.top.RatingTop;
import ru.mike.moviebot.dto.Movie;
import ru.mike.moviebot.handler.IButtonHandler;
import ru.mike.moviebot.keyboard.KeyboardUtils;
import ru.mike.moviebot.service.common.TelegramService;
import ru.mike.moviebot.service.database.MovieListService;
import ru.mike.moviebot.service.database.RatingTopService;
import ru.mike.moviebot.service.database.UserService;
import ru.mike.moviebot.service.movieservice.GenresTopMovieService;
import ru.mike.moviebot.service.statemachineService.BotStateMachineService;
import ru.mike.moviebot.statemachine.event.BotEvent;
import ru.mike.moviebot.statemachine.state.BotState;

import java.util.List;

@Component
public class ContinueRatingTopHandler implements TelegramSending, IButtonHandler {
    @Autowired
    private KeyboardUtils keyboardUtils;
    @Autowired
    private RatingTopService ratingTopService;
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
        return "82def356-9166-429a-93ea-5c85caec25e2";
    }

    @Override
    public void execute(BotApplication botApplication, Update update) {
        stateMachineService.reachState(BotState.CONTINUE_RATING,
                BotEvent.REACH_CONTINUE_RATING, TelegramService.getMessage(update).getChatId());

        movieListService.unselectAllMovieLists(TelegramService.getMessage(update).getChatId());

        RatingTop ratingTop = ratingTopService.findRatingTopByChatId(TelegramService.getMessage(update).getChatId());

        setContinueRating(botApplication, update, ratingTop);
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

    private void setContinueRating(BotApplication botApplication, Update update, RatingTop newRatingTop) {
        List<Movie> movieList = genresTopMovieService
                .findGenresTopMoviesByRating(newRatingTop.getPage());

        if (newRatingTop.getMovieNum() == movieList.size() || movieList.isEmpty()) {
            newRatingTop.setMovieNum(0);
            newRatingTop.setPage(newRatingTop.getPage() + 1);
            movieList = genresTopMovieService.findGenresTopMoviesByRating(newRatingTop.getPage());
        }

        Integer currentMovieId = movieList.get(newRatingTop.getMovieNum()).getId();
        userService.setCurrentMovieIdIntoUser(TelegramService.getMessage(update).getChatId(), currentMovieId);

        //удаляем клавиатуру у текущего сообщения
        EditMessageCaption editMessageCaption = new EditMessageCaption();
        editMessageCaption
                .setChatId(String.valueOf(TelegramService.getMessage(update).getChatId()))
                .setMessageId(TelegramService.getMessage(update).getMessageId())
                .setCaption(TelegramService.getMessage(update).getCaption())
                .setReplyMarkup(keyboardUtils.commonKeyboards().setEmptyKeyboard());
        sendBotEditCaption(botApplication, editMessageCaption);

        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto
                .setChatId(TelegramService.getMessage(update).getChatId())
                .setPhoto(movieList.get(newRatingTop.getMovieNum()).getUrlPoster())
                .setCaption(movieList.get(newRatingTop.getMovieNum()).toString());

        //если у юзера есть доступные списки, которые не были еще выбраны, то выводим их
        if (keyboardUtils.addingToListKeyboard().setInlineShowAvailableMovieListsButtons(update).isPresent()) {
            sendPhoto.setReplyMarkup(keyboardUtils
                    .continueRatingTopKeyboard().setInlineContinueByRatingAndAddToListTopButtons());
        } else {
            sendPhoto.setReplyMarkup(keyboardUtils
                    .continueRatingTopKeyboard().setInlineContinueByRatingTopButtons());
        }


        sendBotPhoto(botApplication, sendPhoto);

        newRatingTop.setMovieNum(newRatingTop.getMovieNum() + 1);
        ratingTopService.saveRatingTop(newRatingTop);
    }
}