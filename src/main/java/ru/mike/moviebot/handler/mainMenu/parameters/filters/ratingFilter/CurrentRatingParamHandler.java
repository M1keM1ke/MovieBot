package ru.mike.moviebot.handler.mainMenu.parameters.filters.ratingFilter;

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

@Component
public class CurrentRatingParamHandler implements TelegramSending, IButtonHandler {
    @Autowired
    private UserService userService;
    @Autowired
    private BotStateMachineService stateMachineService;
    @Autowired
    private ParamMovieService paramMovieService;
    @Autowired
    private KeyboardUtils keyboardUtils;
    @Autowired
    private HandlerService handlerService;

    @Override
    public String getMessage(Update update, String userMessage) {
        if (update.hasCallbackQuery()) {
            return "default";
        }
        User user = userService.findUserByChatId(TelegramService.getMessage(update).getChatId());
        return user.getUserState() == BotState.RATING_PARAM &&
                !handlerService.getMainMenuButtonNames().contains(userMessage)
                ? userMessage : "default";
    }

    @Override
    public void execute(BotApplication botApplication, Update update) {
        stateMachineService.reachState(BotState.CURRENT_RATING_PARAM,
                BotEvent.REACH_CURRENT_RATING_PARAM, TelegramService.getMessage(update).getChatId());

        SendMessage sendMessage = new SendMessage();
        sendMessage
                .setChatId(TelegramService.getMessage(update).getChatId())
                .setReplyMarkup(keyboardUtils.paramMovieKeyboard().setInlineCustomMoviesButtons());

        User user = userService.findUserByChatId(TelegramService.getMessage(update).getChatId());

        ParamMovie paramMovie = paramMovieService.findParamMovieByChatId(user.getChatId());

        Float rating = Float.parseFloat(
                TelegramService.getMessage(update).getText().replaceAll("[\\D]", ""));

        if (rating >= 0 && rating <= 10) {
            sendMessage
                    .setText("Вы ввели рейтинг: " + rating);
            sendBotMessage(botApplication, sendMessage);

            paramMovie.setPopularity(rating);
            paramMovieService.saveParamMovie(paramMovie);
        } else {
            sendMessage
                    .setText("рейтинг неправильный");

        }

    }
}
