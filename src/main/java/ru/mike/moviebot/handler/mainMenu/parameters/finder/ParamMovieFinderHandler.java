package ru.mike.moviebot.handler.mainMenu.parameters.finder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.mike.moviebot.BotApplication;
import ru.mike.moviebot.api.telegram.sending.TelegramSending;
import ru.mike.moviebot.domain.User;
import ru.mike.moviebot.domain.param.ParamMovie;
import ru.mike.moviebot.dto.Movie;
import ru.mike.moviebot.handler.IButtonHandler;
import ru.mike.moviebot.keyboard.KeyboardUtils;
import ru.mike.moviebot.service.common.TelegramService;
import ru.mike.moviebot.service.database.ParamMovieService;
import ru.mike.moviebot.service.database.UserService;
import ru.mike.moviebot.service.statemachineService.BotStateMachineService;
import ru.mike.moviebot.statemachine.event.BotEvent;
import ru.mike.moviebot.statemachine.state.BotState;

import java.util.List;

@Component
public class ParamMovieFinderHandler implements TelegramSending, IButtonHandler {
    @Autowired
    private ParamMovieService paramMovieService;
    @Autowired
    private UserService userService;
    @Autowired
    private KeyboardUtils keyboardUtils;
    @Autowired
    private BotStateMachineService stateMachineService;

    @Override
    public String getMessage() {
        return "45dbda00-5b85-48be-bb3d-c21fec28f2be";
    }

    @Override
    public void execute(BotApplication botApplication, Update update) {
        stateMachineService.reachState(BotState.FIND_PARAM_MOVIE,
                BotEvent.REACH_FIND_PARAM_MOVIE, TelegramService.getMessage(update).getChatId());

        paramMovieService.disableParamEntity(update);

        ParamMovie paramMovie = paramMovieService.findParamMovieByChatId(TelegramService.getMessage(update).getChatId());

        List<Movie> movieList = paramMovieService.findCustomMovies(paramMovie);

        if (!movieList.isEmpty()) {
            DeleteMessage deleteMessage = new DeleteMessage();
            deleteMessage.setChatId(TelegramService.getMessage(update).getChatId());
            deleteMessage.setMessageId(TelegramService.getMessage(update).getMessageId());
            deleteBotMessage(botApplication, deleteMessage);

            SendPhoto sendPhoto = new SendPhoto();
            sendPhoto
                    .setChatId(TelegramService.getMessage(update).getChatId())
                    .setPhoto(movieList.get(0).getUrlPoster())
                    .setCaption(movieList.get(0).toString());

            User userByChatId = userService.findUserByChatId(TelegramService.getMessage(update).getChatId());
            if (userByChatId.getMovieList().isEmpty()) {
                sendPhoto.setReplyMarkup(keyboardUtils
                        .continueParamMovieKeyboard().setInlineContinueParamMovieButtons());
            } else {
                sendPhoto.setReplyMarkup(keyboardUtils
                        .continueParamMovieKeyboard().setInlineContinueByParamAndAddToListTopButtons());
            }
            sendBotPhoto(botApplication, sendPhoto);

            Movie currentMovie = movieList.get(paramMovie.getMovieNum());
            userService.setCurrentMovieIdIntoUser(TelegramService.getMessage(update).getChatId(), currentMovie.getId());
        } else {
            EditMessageText editMessageText = new EditMessageText();
            editMessageText
                    .setChatId(TelegramService.getMessage(update).getChatId())
                    .setMessageId(TelegramService.getMessage(update).getMessageId())
                    .setText("Я не смог найти фильмов с такими параметрами( Может, попробуешь другие?")
                    .setReplyMarkup(keyboardUtils.paramMovieKeyboard().setInlineCustomMoviesButtons());
            sendBotEditMessage(botApplication, editMessageText);
        }
    }
}
