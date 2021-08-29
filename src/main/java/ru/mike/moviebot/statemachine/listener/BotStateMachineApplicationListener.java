package ru.mike.moviebot.statemachine.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;
import ru.mike.moviebot.statemachine.event.BotEvent;
import ru.mike.moviebot.statemachine.state.BotState;

@Slf4j
public class BotStateMachineApplicationListener extends StateMachineListenerAdapter<BotState, BotEvent> {

    @Override
    public void stateChanged(State<BotState, BotEvent> from, State<BotState, BotEvent> to) {
        try{
            if (from.getId() != null) {
                log.info(String.format("Changed state from:%s to:%s", from.getId(), to.getId()));
            }
        } catch (NullPointerException ignored) {

        }
    }

    @Override
    public void transitionStarted(Transition<BotState, BotEvent> transition) {
        if (transition.getSource() != null) {
            log.info(String.format("transition with source %s and with target %s started",
                    transition.getSource().getId(), transition.getTarget().getId()));
        }
    }

    @Override
    public void transitionEnded(Transition<BotState, BotEvent> transition) {
        if (transition.getSource() != null) {
            log.info(String.format("transition with source %s and with target %s ended",
                    transition.getSource().getId(), transition.getTarget().getId()));
        }
    }
}
