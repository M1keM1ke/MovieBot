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
import ru.mike.moviebot.domain.User;
import ru.mike.moviebot.domain.top.YearTop;
import ru.mike.moviebot.dto.Movie;
import ru.mike.moviebot.handler.IButtonHandler;
import ru.mike.moviebot.keyboard.KeyboardUtils;
import ru.mike.moviebot.service.common.TelegramService;
import ru.mike.moviebot.service.database.MovieListService;
import ru.mike.moviebot.service.database.UserService;
import ru.mike.moviebot.service.database.YearTopService;
import ru.mike.moviebot.service.movieservice.GenresTopMovieService;
import ru.mike.moviebot.service.statemachineService.BotStateMachineService;
import ru.mike.moviebot.statemachine.event.BotEvent;
import ru.mike.moviebot.statemachine.state.BotState;

import java.util.List;

@Component
public class ContinueYearTopHandler implements TelegramSending, IButtonHandler {
    @Autowired
    private UserService userService;
    @Autowired
    private YearTopService yearTopService;
    @Autowired
    private KeyboardUtils keyboardUtils;
    @Autowired
    private BotStateMachineService stateMachineService;
    @Autowired
    private GenresTopMovieService genresTopMovieService;
    @Autowired
    private MovieListService movieListService;

    @Override
    public String getMessage() {
        return "78d32dc8-d445-448d-8a78-c294371f8043";
    }

    @Override
    public void execute(BotApplication botApplication, Update update) {
        stateMachineService.reachState(BotState.CONTINUE_YEAR,
                BotEvent.REACH_CONTINUE_YEAR, TelegramService.getMessage(update).getChatId());

        movieListService.unselectAllMovieLists(TelegramService.getMessage(update).getChatId());

        YearTop yearTop = yearTopService.findYearTopByChatId(TelegramService.getMessage(update).getChatId());

        setContinueYear(botApplication, update, yearTop);
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

    private void setContinueYear(BotApplication botApplication, Update update, YearTop newYearTop) {
        List<Movie> movieList = genresTopMovieService
                .findGenresTopMoviesByYear(newYearTop.getYear(), newYearTop.getPage());

        if (newYearTop.getMovieNum() == movieList.size() || movieList.isEmpty()) {
            newYearTop.setMovieNum(0);
            newYearTop.setPage(newYearTop.getPage() + 1);

            movieList = genresTopMovieService.findGenresTopMoviesByYear(newYearTop.getYear(), newYearTop.getPage());
        }

        Integer currentMovieId = movieList.get(newYearTop.getMovieNum()).getId();
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
                .setPhoto(movieList.get(newYearTop.getMovieNum()).getUrlPoster())
                .setCaption(movieList.get(newYearTop.getMovieNum()).toString());

        User userByChatId = userService.findUserByChatId(TelegramService.getMessage(update).getChatId());
        if (userByChatId.getMovieList().isEmpty()) {
            sendPhoto.setReplyMarkup(keyboardUtils
                    .continueYearTopKeyboard().setInlineContinueByYearTopButtons());
        } else {
            sendPhoto.setReplyMarkup(keyboardUtils
                    .continueYearTopKeyboard().setInlineContinueByYearAndAddToListTopButtons());
        }
        sendBotPhoto(botApplication, sendPhoto);

        newYearTop.setMovieNum(newYearTop.getMovieNum() + 1);
        yearTopService.saveYearTop(newYearTop);
    }
}