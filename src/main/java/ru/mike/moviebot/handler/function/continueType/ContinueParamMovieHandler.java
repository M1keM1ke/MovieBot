package ru.mike.moviebot.handler.function.continueType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import ru.mike.moviebot.BotApplication;
import ru.mike.moviebot.api.telegram.sending.TelegramSending;
import ru.mike.moviebot.domain.param.ParamMovie;
import ru.mike.moviebot.dto.Movie;
import ru.mike.moviebot.handler.IButtonHandler;
import ru.mike.moviebot.keyboard.KeyboardUtils;
import ru.mike.moviebot.service.common.TelegramService;
import ru.mike.moviebot.service.database.MovieListService;
import ru.mike.moviebot.service.database.ParamMovieService;
import ru.mike.moviebot.service.database.UserService;
import ru.mike.moviebot.service.statemachineService.BotStateMachineService;
import ru.mike.moviebot.statemachine.event.BotEvent;
import ru.mike.moviebot.statemachine.state.BotState;

import java.util.List;

@Component
public class ContinueParamMovieHandler implements TelegramSending, IButtonHandler {
    @Autowired
    private KeyboardUtils keyboardUtils;
    @Autowired
    private ParamMovieService paramMovieService;
    @Autowired
    private BotStateMachineService stateMachineService;
    @Autowired
    private MovieListService movieListService;
    @Autowired
    private UserService userService;

    @Override
    public String getMessage() {
        return "e460d897-37cb-4767-9fa5-4d99268108a9";
    }

    @Override
    public void execute(BotApplication botApplication, Update update) {
        stateMachineService.reachState(BotState.CONTINUE_PARAM,
                BotEvent.REACH_CONTINUE_PARAM, TelegramService.getMessage(update).getChatId());

        movieListService.unselectAllMovieLists(TelegramService.getMessage(update).getChatId());

        ParamMovie paramMovie = paramMovieService.findParamMovieByChatId(TelegramService.getMessage(update).getChatId());
        setContinueParamMovie(botApplication, update, paramMovie);
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

    private void setContinueParamMovie(BotApplication botApplication, Update update, ParamMovie paramMovie) {
        EditMessageCaption editMessageCaption = new EditMessageCaption();
        editMessageCaption
                .setChatId(TelegramService.getMessage(update).getChatId().toString())
                .setMessageId(TelegramService.getMessage(update).getMessageId())
                .setCaption(TelegramService.getMessage(update).getCaption())
                .setReplyMarkup(keyboardUtils.commonKeyboards().setEmptyKeyboard());
        sendBotEditCaption(botApplication, editMessageCaption);

        List<Movie> movieList = paramMovieService.findCustomMovies(paramMovie);

        paramMovie.setMovieNum(paramMovie.getMovieNum() + 1);

        SendPhoto sendPhoto = new SendPhoto();
        if (keyboardUtils.addingToListKeyboard().setInlineShowAvailableMovieListsButtons(update).isPresent()) {
            sendPhoto.setReplyMarkup(keyboardUtils
                    .continueParamMovieKeyboard().setInlineContinueByParamAndAddToListTopButtons());
        } else {
            sendPhoto.setReplyMarkup(keyboardUtils
                    .continueParamMovieKeyboard().setInlineContinueParamMovieButtons());
        }

        if (paramMovie.getMovieNum() != movieList.size()) {
            sendPhoto
                    .setChatId(TelegramService.getMessage(update).getChatId())
                    .setPhoto(movieList.get(paramMovie.getMovieNum()).getUrlPoster())
                    .setCaption(movieList.get(paramMovie.getMovieNum()).toString());
            sendBotPhoto(botApplication, sendPhoto);

        } else { //если фильмы на текущей странице закончились, то обнуляем счетчик фильмов
            paramMovie.setMovieNum(0);
            paramMovie.setPage(paramMovie.getPage() + 1);

            movieList = paramMovieService.findCustomMovies(paramMovie);
            if (movieList.isEmpty()) { //если на новой странице фильмов нет, то фильмы закончились
                paramMovieService.disableParamEntity(update);

                SendMessage sendMessage1 = new SendMessage();
                sendMessage1
                        .setChatId(TelegramService.getMessage(update).getChatId())
                        .setText("Ой, фильмы с такими параметрами закончились(\nХочешь что-то поменять?")
                        .setReplyMarkup(keyboardUtils.paramMovieKeyboard().setInlineCustomMoviesButtons());
                sendBotMessage(botApplication, sendMessage1);
                return;
            } else {
                sendBotEditCaption(botApplication, editMessageCaption);

                sendPhoto
                        .setChatId(TelegramService.getMessage(update).getChatId())
                        .setPhoto(movieList.get(paramMovie.getMovieNum()).getUrlPoster())
                        .setCaption(movieList.get(paramMovie.getMovieNum()).toString());
                sendBotPhoto(botApplication, sendPhoto);
            }
        }
        paramMovieService.saveParamMovie(paramMovie);

        Movie currentMovie = movieList.get(paramMovie.getMovieNum());
        userService.setCurrentMovieIdIntoUser(TelegramService.getMessage(update).getChatId(), currentMovie.getId());
    }
}