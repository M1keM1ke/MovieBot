package ru.mike.moviebot.handler.mainMenu.parameters.filters.actorFilter;

import com.uwetrottmann.tmdb2.entities.BasePerson;
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
import ru.mike.moviebot.service.actor.PersonService;
import ru.mike.moviebot.service.common.TelegramService;
import ru.mike.moviebot.service.database.ParamMovieService;
import ru.mike.moviebot.service.database.UserService;
import ru.mike.moviebot.service.handler.HandlerService;
import ru.mike.moviebot.service.statemachineService.BotStateMachineService;
import ru.mike.moviebot.statemachine.event.BotEvent;
import ru.mike.moviebot.statemachine.state.BotState;

import java.util.Optional;

@Component
public class CurrentActorParamHandler implements TelegramSending, IButtonHandler {
    @Autowired
    private UserService userService;
    @Autowired
    private BotStateMachineService stateMachineService;
    @Autowired
    private PersonService personService;
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
        return user.getUserState() == BotState.ACTOR_PARAM &&
                !handlerService.getMainMenuButtonNames().contains(userMessage)
                ? userMessage : "default";
    }

    @Override
    public void execute(BotApplication botApplication, Update update) {
        stateMachineService.reachState(BotState.CURRENT_ACTOR_PARAM,
                BotEvent.REACH_CURRENT_ACTOR_PARAM, TelegramService.getMessage(update).getChatId());

        SendMessage sendMessage = new SendMessage();
        sendMessage
                .setChatId(TelegramService.getMessage(update).getChatId())
                .setReplyMarkup(keyboardUtils.paramMovieKeyboard().setInlineCustomMoviesButtons());

        User user = userService.findUserByChatId(TelegramService.getMessage(update).getChatId());
        Optional<BasePerson> actor = personService.findPersonByName(TelegramService.getMessage(update).getText());

        if (actor.isPresent()) {
            sendMessage
                    .setText("Вы ввели актера: " + actor.get().name);
            sendBotMessage(botApplication, sendMessage);

            ParamMovie paramMovie = paramMovieService.findParamMovieByChatId(user.getChatId());
            paramMovie.setActor(actor.get().name);
            paramMovie.setActorId(actor.get().id);
            paramMovieService.saveParamMovie(paramMovie);
        } else {
            sendMessage
                    .setText("Я не смог найти такого актера...Попробуешь еще?)");
            sendBotMessage(botApplication, sendMessage);
        }
    }
}