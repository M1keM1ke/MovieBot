package ru.mike.moviebot.handler.mainMenu.parameters.filters.yearFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.mike.moviebot.BotApplication;
import ru.mike.moviebot.api.telegram.sending.TelegramSending;
import ru.mike.moviebot.domain.User;
import ru.mike.moviebot.domain.param.ParamMovie;
import ru.mike.moviebot.handler.IButtonHandler;
import ru.mike.moviebot.keyboard.KeyboardUtils;
import ru.mike.moviebot.service.common.TelegramService;
import ru.mike.moviebot.service.database.ParamMovieService;
import ru.mike.moviebot.service.database.UserService;
import ru.mike.moviebot.service.handler.HandlerService;
import ru.mike.moviebot.service.statemachineService.BotStateMachineService;
import ru.mike.moviebot.statemachine.event.BotEvent;
import ru.mike.moviebot.statemachine.state.BotState;

import java.util.Calendar;

@Component
public class CurrentYearParamHandler implements TelegramSending, IButtonHandler {
    @Autowired
    private UserService userService;
    @Autowired
    private ParamMovieService paramMovieService;
    @Autowired
    private KeyboardUtils keyboardUtils;
    @Autowired
    private BotStateMachineService stateMachineService;
    @Autowired
    private HandlerService handlerService;

    @Override
    public String getMessage(Update update, String userMessage) {
        if (update.hasCallbackQuery()) {
            return "default";
        }
        User user = userService.findUserByChatId(TelegramService.getMessage(update).getChatId());
        return user.getUserState() == BotState.YEAR_PARAM &&
                !handlerService.getMainMenuButtonNames().contains(userMessage)
                ? userMessage : "default";
    }

    @Override
    public void execute(BotApplication botApplication, Update update) {
        stateMachineService.reachState(BotState.CURRENT_YEAR_PARAM,
                BotEvent.REACH_CURRENT_YEAR_PARAM, TelegramService.getMessage(update).getChatId());

        int year = Integer.parseInt(TelegramService.getMessage(update).getText().replaceAll("[\\D]", ""));
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        SendMessage sendMessage = new SendMessage();
        User user = userService.findUserByChatId(TelegramService.getMessage(update).getChatId());
        ParamMovie paramMovie = paramMovieService.findParamMovieByChatId(user.getChatId());

        if (year >= 1950 && year <= currentYear) {
            sendMessage
                    .setChatId(TelegramService.getMessage(update).getChatId())
                    .setText("Вы ввели год: " + year)
                    .setReplyMarkup(keyboardUtils.paramMovieKeyboard().setInlineCustomMoviesButtons());
            sendBotMessage(botApplication, sendMessage);

            paramMovie.setYear(year);
            paramMovieService.saveParamMovie(paramMovie);
        }
    }
}