package ru.mike.moviebot.handler.mainMenu.tops.ratingTop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.mike.moviebot.BotApplication;
import ru.mike.moviebot.api.telegram.sending.TelegramSending;
import ru.mike.moviebot.domain.User;
import ru.mike.moviebot.domain.top.RatingTop;
import ru.mike.moviebot.dto.Movie;
import ru.mike.moviebot.handler.IButtonHandler;
import ru.mike.moviebot.keyboard.KeyboardUtils;
import ru.mike.moviebot.service.common.TelegramService;
import ru.mike.moviebot.service.database.RatingTopService;
import ru.mike.moviebot.service.database.UserService;
import ru.mike.moviebot.service.movieservice.GenresTopMovieService;
import ru.mike.moviebot.service.statemachineService.BotStateMachineService;
import ru.mike.moviebot.statemachine.event.BotEvent;
import ru.mike.moviebot.statemachine.state.BotState;

import java.util.List;

@Component
public class RatingTopMovieHandler implements TelegramSending, IButtonHandler {
    @Autowired
    KeyboardUtils keyboardUtils;
    @Autowired
    BotStateMachineService stateMachineService;
    @Autowired
    GenresTopMovieService genresTopMovieService;
    @Autowired
    RatingTopService ratingTopService;
    @Autowired
    UserService userService;

    @Override
    public String getMessage() {
        return "2f9c70ca-a8ce-417d-83bf-ad3772788938";
    }

    @Override
    public void execute(BotApplication botApplication, Update update) {
        stateMachineService.reachState(BotState.RATING_TOP,
                BotEvent.REACH_RATING_TOP, TelegramService.getMessage(update).getChatId());

        RatingTop ratingTop = ratingTopService.findRatingTopByChatId(TelegramService.getMessage(update).getChatId());
        List<Movie> movieList = genresTopMovieService.findGenresTopMoviesByRating(ratingTop.getPage());

        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage
                .setChatId(TelegramService.getMessage(update).getChatId())
                .setMessageId(TelegramService.getMessage(update).getMessageId());
        deleteBotMessage(botApplication, deleteMessage);

        Movie currentMovie = movieList.get(ratingTop.getMovieNum());


        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto
                .setChatId(TelegramService.getMessage(update).getChatId())
                .setPhoto(currentMovie.getUrlPoster())
                .setCaption(currentMovie.toString());

        User userByChatId = userService.findUserByChatId(TelegramService.getMessage(update).getChatId());
        if (userByChatId.getMovieList().isEmpty()) {
            sendPhoto.setReplyMarkup(keyboardUtils
                    .continueRatingTopKeyboard().setInlineContinueByRatingTopButtons());
        } else {
            sendPhoto.setReplyMarkup(keyboardUtils
                    .continueRatingTopKeyboard().setInlineContinueByRatingAndAddToListTopButtons());
        }
        sendBotPhoto(botApplication, sendPhoto);

        userService.setCurrentMovieIdIntoUser(TelegramService.getMessage(update).getChatId(), currentMovie.getId());

        ratingTop.setMovieNum(ratingTop.getMovieNum() + 1);
        ratingTopService.saveRatingTop(ratingTop);
    }
}
