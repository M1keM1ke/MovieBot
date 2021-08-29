package ru.mike.moviebot.service.statemachineService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;
import ru.mike.moviebot.domain.User;
import ru.mike.moviebot.service.database.UserService;
import ru.mike.moviebot.statemachine.event.BotEvent;
import ru.mike.moviebot.statemachine.state.BotState;

@Service
@SuppressWarnings("all")
public class BotStateMachineService {
    @Autowired
    private StateMachineFactory<BotState, BotEvent> stateMachineFactory;
    @Autowired
    private UserService userService;

    public boolean reachState(BotState reachState, BotEvent botEvent, Long userChatId) {
        StateMachine<BotState, BotEvent> stateMachine = build(userChatId);
        stateMachine.sendEvent(botEvent);

        User user = userService.findUserByChatId(userChatId);
        user.setUserState(reachState);
        userService.saveUser(user);
        return true;
    }

    private StateMachine<BotState, BotEvent> build(Long userChatId) {
        StateMachine<BotState, BotEvent> stateMachine = stateMachineFactory.getStateMachine();
        User user = userService.findUserByChatId(userChatId);

        stateMachine
                .getStateMachineAccessor()
                .doWithAllRegions(access -> {
                    access.resetStateMachine(new DefaultStateMachineContext<>(user.getUserState(),
                            null, null, null, null));
                });
        stateMachine.getExtendedState().getVariables().put("USER_ID", String.valueOf(userChatId));
        stateMachine.start();
        return stateMachine;
    }
}
