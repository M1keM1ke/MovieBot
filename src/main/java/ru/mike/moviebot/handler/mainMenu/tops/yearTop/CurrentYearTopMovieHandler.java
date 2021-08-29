package ru.mike.moviebot.handler.mainMenu.tops.yearTop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.mike.moviebot.BotApplication;
import ru.mike.moviebot.api.telegram.sending.TelegramSending;
import ru.mike.moviebot.domain.User;
import ru.mike.moviebot.domain.top.YearTop;
import ru.mike.moviebot.dto.Movie;
import ru.mike.moviebot.handler.IButtonHandler;
import ru.mike.moviebot.keyboard.KeyboardUtils;
import ru.mike.moviebot.service.common.TelegramService;
import ru.mike.moviebot.service.database.UserService;
import ru.mike.moviebot.service.database.YearTopService;
import ru.mike.moviebot.service.handler.HandlerService;
import ru.mike.moviebot.service.movieservice.GenresTopMovieService;
import ru.mike.moviebot.service.statemachineService.BotStateMachineService;
import ru.mike.moviebot.statemachine.event.BotEvent;
import ru.mike.moviebot.statemachine.state.BotState;

import java.util.Calendar;
import java.util.List;

@Component
public class CurrentYearTopMovieHandler implements TelegramSending, IButtonHandler {
    @Autowired
    private KeyboardUtils keyboardUtils;
    @Autowired
    private UserService userService;
    @Autowired
    private BotStateMachineService stateMachineService;
    @Autowired
    private GenresTopMovieService genresTopMovieService;
    @Autowired
    private YearTopService yearTopService;
    @Autowired
    private HandlerService handlerService;

    @Override
    public String getMessage(Update update, String userMessage) {
        if (update.hasCallbackQuery()) {
            return "default";
        }
        User user = userService.findUserByChatId(TelegramService.getMessage(update).getChatId());
        return user.getUserState() == BotState.YEAR_TOP &&
                !handlerService.getMainMenuButtonNames().contains(userMessage)
                ? userMessage : "default";
    }

    @Override
    public void execute(BotApplication botApplication, Update update) {
        stateMachineService.reachState(BotState.CURRENT_YEAR_TOP,
                BotEvent.REACH_CURRENT_YEAR_TOP, TelegramService.getMessage(update).getChatId());

        int year = Integer.parseInt(TelegramService.getMessage(update).getText().replaceAll("[\\D]", ""));
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        YearTop yearTop = yearTopService.findYearTopByChatId(TelegramService.getMessage(update).getChatId());

        if (year >= 1950 && year <= currentYear) {
            yearTop.setYear(year);

            List<Movie> movieList = genresTopMovieService
                    .findGenresTopMoviesByYear(year, yearTop.getPage());

            SendPhoto sendPhoto = new SendPhoto();
            sendPhoto
                    .setChatId(TelegramService.getMessage(update).getChatId())
                    .setPhoto(movieList.get(yearTop.getMovieNum()).getUrlPoster())
                    .setCaption(movieList.get(yearTop.getMovieNum()).toString());

            User userByChatId = userService.findUserByChatId(TelegramService.getMessage(update).getChatId());
            if (userByChatId.getMovieList().isEmpty()) {
                sendPhoto.setReplyMarkup(keyboardUtils
                        .continueYearTopKeyboard().setInlineContinueByYearTopButtons());
            } else {
                sendPhoto.setReplyMarkup(keyboardUtils
                        .continueYearTopKeyboard().setInlineContinueByYearAndAddToListTopButtons());
            }
            sendBotPhoto(botApplication, sendPhoto);

            yearTopService.disableYearEntity(update);

            Movie currentMovie = movieList.get(yearTop.getMovieNum());
            userService.setCurrentMovieIdIntoUser(TelegramService.getMessage(update).getChatId(), currentMovie.getId());

            yearTop.setMovieNum(yearTop.getMovieNum() + 1);
            yearTopService.saveYearTop(yearTop);

        } else {
            SendMessage sendMessage = new SendMessage();
            sendMessage
                    .setChatId(TelegramService.getMessage(update).getChatId())
                    .setText("я не понимаю( введи корректный год");
            sendBotMessage(botApplication, sendMessage);
        }
    }
}

