package ru.mike.moviebot.statemachine.stateConfig;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import ru.mike.moviebot.statemachine.event.BotEvent;
import ru.mike.moviebot.statemachine.listener.BotStateMachineApplicationListener;
import ru.mike.moviebot.statemachine.state.BotState;

import java.util.EnumSet;

@Configuration
@EnableStateMachineFactory
public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<BotState, BotEvent> {
    private Logger log = LogManager.getLogger(StateMachineConfig.class);

    @Override
    public void configure(StateMachineConfigurationConfigurer config) throws Exception {
        config
                .withConfiguration()
                .autoStartup(true)
                .listener(new BotStateMachineApplicationListener());
    }

    @Override
    public void configure(StateMachineStateConfigurer<BotState, BotEvent> states) throws Exception {
        states
                .withStates()
                .initial(BotState.NEW)
                .states(EnumSet.allOf(BotState.class))
                .end(BotState.END);
    }



    @Override
    public void configure(StateMachineTransitionConfigurer<BotState, BotEvent> transitions) throws Exception {
        //DOWN DESTINATION
        setUp0to1DownLayer(transitions);
        setUp1to2DownLayer(transitions);
        setUp2to3DownLayer(transitions);
        setUp3to4DownLayer(transitions);

        //UP DESTINATION
        setUp2to1UpLayer(transitions);
        setUp3to2UpLayer(transitions);
        setUp3to1UpLayer(transitions);
        setUp4to3UpLayer(transitions);

        //MIDDLE DESTINATION
        setUp1to1MiddleLayer(transitions);
    }

    @Bean
    public Action<BotState, BotEvent> reachStateAction() {
        return stateContext -> {
            String userId = stateContext.getExtendedState().get("USER_ID", String.class);
            log.log(Level.INFO,
                    String.format("User with id=%s reach %s", userId, stateContext.getTarget().getId()));
        };
    }

    private void setUp0to1DownLayer(
            StateMachineTransitionConfigurer<BotState, BotEvent> transitions) throws Exception{
        transitions
                //*****************FIRST LAYER*********************
                .withExternal()
                .source(BotState.NEW)
                .target(BotState.RANDOM)
                .event(BotEvent.REACH_RANDOM)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.NEW)
                .target(BotState.TOP)
                .event(BotEvent.REACH_TOP)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.NEW)
                .target(BotState.PARAM)
                .event(BotEvent.REACH_PARAM)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.NEW)
                .target(BotState.LIST)
                .event(BotEvent.REACH_LIST)
                .action(reachStateAction());
    }

    private void setUp1to2DownLayer(
            StateMachineTransitionConfigurer<BotState, BotEvent> transitions) throws Exception {
        transitions
                //*****************SECOND LAYER*********************
                .withExternal()
                .source(BotState.TOP)
                .target(BotState.GENRE_TOP)
                .event(BotEvent.REACH_GENRE_TOP)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.TOP)
                .target(BotState.YEAR_TOP)
                .event(BotEvent.REACH_YEAR_TOP)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.TOP)
                .target(BotState.RATING_TOP)
                .event(BotEvent.REACH_RATING_TOP)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.PARAM)
                .target(BotState.GENRE_PARAM)
                .event(BotEvent.REACH_GENRE_PARAM)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.PARAM)
                .target(BotState.YEAR_PARAM)
                .event(BotEvent.REACH_YEAR_PARAM)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.PARAM)
                .target(BotState.RATING_PARAM)
                .event(BotEvent.REACH_RATING_PARAM)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.PARAM)
                .target(BotState.ACTOR_PARAM)
                .event(BotEvent.REACH_ACTOR_PARAM)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.LIST)
                .target(BotState.NEW_LIST)
                .event(BotEvent.REACH_NEW_LIST)
                .action(reachStateAction());
    }

    private void setUp2to3DownLayer(
            StateMachineTransitionConfigurer<BotState, BotEvent> transitions) throws Exception {
        transitions
                //*****************THIRD LAYER*********************
                .withExternal()
                .source(BotState.GENRE_TOP)
                .target(BotState.CURRENT_GENRE_TOP)
                .event(BotEvent.REACH_CURRENT_GENRE_TOP)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.YEAR_TOP)
                .target(BotState.CURRENT_YEAR_TOP)
                .event(BotEvent.REACH_CURRENT_YEAR_TOP)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.GENRE_PARAM)
                .target(BotState.CURRENT_GENRE_PARAM)
                .event(BotEvent.REACH_CURRENT_GENRE_PARAM)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.YEAR_PARAM)
                .target(BotState.CURRENT_YEAR_PARAM)
                .event(BotEvent.REACH_CURRENT_YEAR_PARAM)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.RATING_PARAM)
                .target(BotState.CURRENT_RATING_PARAM)
                .event(BotEvent.REACH_CURRENT_RATING_PARAM)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.ACTOR_PARAM)
                .target(BotState.CURRENT_ACTOR_PARAM)
                .event(BotEvent.REACH_CURRENT_ACTOR_PARAM)
                .action(reachStateAction());
    }

    private void setUp3to4DownLayer(
            StateMachineTransitionConfigurer<BotState, BotEvent> transitions) throws Exception {
        transitions
                //*****************FOURTH LAYER*********************
                .withExternal()
                .source(BotState.CURRENT_GENRE_TOP)
                .target(BotState.CONTINUE_GENRE)
                .event(BotEvent.REACH_CONTINUE_GENRE)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CURRENT_YEAR_TOP)
                .target(BotState.CONTINUE_YEAR)
                .event(BotEvent.REACH_CONTINUE_YEAR)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.RATING_TOP)
                .target(BotState.CONTINUE_RATING)
                .event(BotEvent.REACH_CONTINUE_RATING)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CURRENT_GENRE_PARAM)
                .target(BotState.CONTINUE_PARAM)
                .event(BotEvent.REACH_CONTINUE_PARAM)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CURRENT_YEAR_PARAM)
                .target(BotState.CONTINUE_PARAM)
                .event(BotEvent.REACH_CONTINUE_PARAM)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CURRENT_RATING_PARAM)
                .target(BotState.CONTINUE_PARAM)
                .event(BotEvent.REACH_CONTINUE_PARAM)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CURRENT_ACTOR_PARAM)
                .target(BotState.CONTINUE_PARAM)
                .event(BotEvent.REACH_CONTINUE_PARAM)
                .action(reachStateAction());
    }

    private void setUp1to1MiddleLayer(
            StateMachineTransitionConfigurer<BotState, BotEvent> transitions) throws Exception {
        transitions
                //*****************FIRST LAYER**********************
                .withExternal()
                .source(BotState.RANDOM)
                .target(BotState.TOP)
                .event(BotEvent.REACH_TOP)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.TOP)
                .target(BotState.RANDOM)
                .event(BotEvent.REACH_RANDOM)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.TOP)
                .target(BotState.PARAM)
                .event(BotEvent.REACH_PARAM)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.PARAM)
                .target(BotState.TOP)
                .event(BotEvent.REACH_TOP)
                .action(reachStateAction());
    }

    private void setUp2to1UpLayer(
            StateMachineTransitionConfigurer<BotState, BotEvent> transitions) throws Exception {
        transitions
                //*****************SECOND LAYER**********************
                .withExternal()
                .source(BotState.GENRE_TOP)
                .target(BotState.RANDOM)
                .event(BotEvent.REACH_RANDOM)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.GENRE_TOP)
                .target(BotState.TOP)
                .event(BotEvent.REACH_TOP)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.GENRE_TOP)
                .target(BotState.PARAM)
                .event(BotEvent.REACH_PARAM)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.YEAR_TOP)
                .target(BotState.RANDOM)
                .event(BotEvent.REACH_RANDOM)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.YEAR_TOP)
                .target(BotState.TOP)
                .event(BotEvent.REACH_TOP)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.YEAR_TOP)
                .target(BotState.PARAM)
                .event(BotEvent.REACH_PARAM)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.RATING_TOP)
                .target(BotState.RANDOM)
                .event(BotEvent.REACH_RANDOM)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.RATING_TOP)
                .target(BotState.TOP)
                .event(BotEvent.REACH_TOP)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.RATING_TOP)
                .target(BotState.PARAM)
                .event(BotEvent.REACH_PARAM)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.GENRE_PARAM)
                .target(BotState.RANDOM)
                .event(BotEvent.REACH_RANDOM)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.GENRE_PARAM)
                .target(BotState.TOP)
                .event(BotEvent.REACH_TOP)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.GENRE_PARAM)
                .target(BotState.PARAM)
                .event(BotEvent.REACH_PARAM)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.YEAR_PARAM)
                .target(BotState.RANDOM)
                .event(BotEvent.REACH_RANDOM)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.YEAR_PARAM)
                .target(BotState.TOP)
                .event(BotEvent.REACH_TOP)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.YEAR_PARAM)
                .target(BotState.PARAM)
                .event(BotEvent.REACH_PARAM)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.RATING_PARAM)
                .target(BotState.RANDOM)
                .event(BotEvent.REACH_RANDOM)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.RATING_PARAM)
                .target(BotState.TOP)
                .event(BotEvent.REACH_TOP)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.RATING_PARAM)
                .target(BotState.PARAM)
                .event(BotEvent.REACH_PARAM)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.ACTOR_PARAM)
                .target(BotState.RANDOM)
                .event(BotEvent.REACH_RANDOM)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.ACTOR_PARAM)
                .target(BotState.TOP)
                .event(BotEvent.REACH_TOP)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.ACTOR_PARAM)
                .target(BotState.PARAM)
                .event(BotEvent.REACH_PARAM)
                .action(reachStateAction());
    }

    private void setUp3to2UpLayer(
            StateMachineTransitionConfigurer<BotState, BotEvent> transitions) throws Exception {
        transitions
                //*****************THIRD LAYER**********************
                // *****************TO SECOND LAYER**********************
                .withExternal()
                .source(BotState.CURRENT_GENRE_TOP)
                .target(BotState.GENRE_TOP)
                .event(BotEvent.REACH_GENRE_TOP)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CURRENT_GENRE_TOP)
                .target(BotState.YEAR_TOP)
                .event(BotEvent.REACH_YEAR_TOP)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CURRENT_GENRE_TOP)
                .target(BotState.RATING_TOP)
                .event(BotEvent.REACH_RATING_TOP)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CURRENT_GENRE_TOP)
                .target(BotState.GENRE_PARAM)
                .event(BotEvent.REACH_GENRE_PARAM)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CURRENT_GENRE_TOP)
                .target(BotState.YEAR_PARAM)
                .event(BotEvent.REACH_YEAR_PARAM)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CURRENT_GENRE_TOP)
                .target(BotState.RATING_PARAM)
                .event(BotEvent.REACH_RATING_PARAM)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CURRENT_GENRE_TOP)
                .target(BotState.ACTOR_PARAM)
                .event(BotEvent.REACH_ACTOR_PARAM)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CURRENT_YEAR_TOP)
                .target(BotState.GENRE_TOP)
                .event(BotEvent.REACH_GENRE_TOP)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CURRENT_YEAR_TOP)
                .target(BotState.YEAR_TOP)
                .event(BotEvent.REACH_YEAR_TOP)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CURRENT_YEAR_TOP)
                .target(BotState.RATING_TOP)
                .event(BotEvent.REACH_RATING_TOP)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CURRENT_YEAR_TOP)
                .target(BotState.GENRE_PARAM)
                .event(BotEvent.REACH_GENRE_PARAM)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CURRENT_YEAR_TOP)
                .target(BotState.YEAR_PARAM)
                .event(BotEvent.REACH_YEAR_PARAM)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CURRENT_YEAR_TOP)
                .target(BotState.RATING_PARAM)
                .event(BotEvent.REACH_RATING_PARAM)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CURRENT_YEAR_TOP)
                .target(BotState.ACTOR_PARAM)
                .event(BotEvent.REACH_ACTOR_PARAM)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CURRENT_GENRE_PARAM)
                .target(BotState.GENRE_TOP)
                .event(BotEvent.REACH_GENRE_TOP)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CURRENT_GENRE_PARAM)
                .target(BotState.YEAR_TOP)
                .event(BotEvent.REACH_YEAR_TOP)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CURRENT_GENRE_PARAM)
                .target(BotState.RATING_TOP)
                .event(BotEvent.REACH_RATING_TOP)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CURRENT_GENRE_PARAM)
                .target(BotState.GENRE_PARAM)
                .event(BotEvent.REACH_GENRE_PARAM)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CURRENT_GENRE_PARAM)
                .target(BotState.YEAR_PARAM)
                .event(BotEvent.REACH_YEAR_PARAM)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CURRENT_GENRE_PARAM)
                .target(BotState.RATING_PARAM)
                .event(BotEvent.REACH_RATING_PARAM)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CURRENT_GENRE_PARAM)
                .target(BotState.ACTOR_PARAM)
                .event(BotEvent.REACH_ACTOR_PARAM)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CURRENT_YEAR_PARAM)
                .target(BotState.GENRE_TOP)
                .event(BotEvent.REACH_GENRE_TOP)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CURRENT_YEAR_PARAM)
                .target(BotState.YEAR_TOP)
                .event(BotEvent.REACH_YEAR_TOP)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CURRENT_YEAR_PARAM)
                .target(BotState.RATING_TOP)
                .event(BotEvent.REACH_RATING_TOP)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CURRENT_YEAR_PARAM)
                .target(BotState.GENRE_PARAM)
                .event(BotEvent.REACH_GENRE_PARAM)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CURRENT_YEAR_PARAM)
                .target(BotState.YEAR_PARAM)
                .event(BotEvent.REACH_YEAR_PARAM)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CURRENT_YEAR_PARAM)
                .target(BotState.RATING_PARAM)
                .event(BotEvent.REACH_RATING_PARAM)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CURRENT_YEAR_PARAM)
                .target(BotState.ACTOR_PARAM)
                .event(BotEvent.REACH_ACTOR_PARAM)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CURRENT_RATING_PARAM)
                .target(BotState.GENRE_TOP)
                .event(BotEvent.REACH_GENRE_TOP)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CURRENT_RATING_PARAM)
                .target(BotState.YEAR_TOP)
                .event(BotEvent.REACH_YEAR_TOP)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CURRENT_RATING_PARAM)
                .target(BotState.RATING_TOP)
                .event(BotEvent.REACH_RATING_TOP)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CURRENT_RATING_PARAM)
                .target(BotState.GENRE_PARAM)
                .event(BotEvent.REACH_GENRE_PARAM)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CURRENT_RATING_PARAM)
                .target(BotState.YEAR_PARAM)
                .event(BotEvent.REACH_YEAR_PARAM)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CURRENT_RATING_PARAM)
                .target(BotState.RATING_PARAM)
                .event(BotEvent.REACH_RATING_PARAM)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CURRENT_RATING_PARAM)
                .target(BotState.ACTOR_PARAM)
                .event(BotEvent.REACH_ACTOR_PARAM)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CURRENT_ACTOR_PARAM)
                .target(BotState.GENRE_TOP)
                .event(BotEvent.REACH_GENRE_TOP)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CURRENT_ACTOR_PARAM)
                .target(BotState.YEAR_TOP)
                .event(BotEvent.REACH_YEAR_TOP)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CURRENT_ACTOR_PARAM)
                .target(BotState.RATING_TOP)
                .event(BotEvent.REACH_RATING_TOP)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CURRENT_ACTOR_PARAM)
                .target(BotState.GENRE_PARAM)
                .event(BotEvent.REACH_GENRE_PARAM)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CURRENT_ACTOR_PARAM)
                .target(BotState.YEAR_PARAM)
                .event(BotEvent.REACH_YEAR_PARAM)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CURRENT_ACTOR_PARAM)
                .target(BotState.RATING_PARAM)
                .event(BotEvent.REACH_RATING_PARAM)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CURRENT_ACTOR_PARAM)
                .target(BotState.ACTOR_PARAM)
                .event(BotEvent.REACH_ACTOR_PARAM)
                .action(reachStateAction());
    }

    private void setUp3to1UpLayer(
            StateMachineTransitionConfigurer<BotState, BotEvent> transitions) throws Exception {
        transitions
                // *****************TO FIRST LAYER**********************
                .withExternal()
                .source(BotState.CURRENT_GENRE_TOP)
                .target(BotState.RANDOM)
                .event(BotEvent.REACH_RANDOM)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CURRENT_GENRE_TOP)
                .target(BotState.TOP)
                .event(BotEvent.REACH_TOP)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CURRENT_GENRE_TOP)
                .target(BotState.PARAM)
                .event(BotEvent.REACH_PARAM)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CURRENT_YEAR_TOP)
                .target(BotState.RANDOM)
                .event(BotEvent.REACH_RANDOM)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CURRENT_YEAR_TOP)
                .target(BotState.TOP)
                .event(BotEvent.REACH_TOP)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CURRENT_YEAR_TOP)
                .target(BotState.PARAM)
                .event(BotEvent.REACH_PARAM)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CURRENT_GENRE_PARAM)
                .target(BotState.RANDOM)
                .event(BotEvent.REACH_RANDOM)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CURRENT_GENRE_PARAM)
                .target(BotState.TOP)
                .event(BotEvent.REACH_TOP)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CURRENT_GENRE_PARAM)
                .target(BotState.PARAM)
                .event(BotEvent.REACH_PARAM)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CURRENT_YEAR_PARAM)
                .target(BotState.RANDOM)
                .event(BotEvent.REACH_RANDOM)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CURRENT_YEAR_PARAM)
                .target(BotState.TOP)
                .event(BotEvent.REACH_TOP)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CURRENT_YEAR_PARAM)
                .target(BotState.PARAM)
                .event(BotEvent.REACH_PARAM)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CURRENT_RATING_PARAM)
                .target(BotState.RANDOM)
                .event(BotEvent.REACH_RANDOM)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CURRENT_RATING_PARAM)
                .target(BotState.TOP)
                .event(BotEvent.REACH_TOP)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CURRENT_RATING_PARAM)
                .target(BotState.PARAM)
                .event(BotEvent.REACH_PARAM)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CURRENT_ACTOR_PARAM)
                .target(BotState.RANDOM)
                .event(BotEvent.REACH_RANDOM)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CURRENT_ACTOR_PARAM)
                .target(BotState.TOP)
                .event(BotEvent.REACH_TOP)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CURRENT_ACTOR_PARAM)
                .target(BotState.PARAM)
                .event(BotEvent.REACH_PARAM)
                .action(reachStateAction());
    }

    private void setUp4to3UpLayer(
            StateMachineTransitionConfigurer<BotState, BotEvent> transitions) throws Exception {
        transitions
                //*****************FOURTH LAYER**********************
                // *****************TO THIRD LAYER**********************
                .withExternal()
                .source(BotState.CONTINUE_GENRE)
                .target(BotState.CURRENT_GENRE_TOP)
                .event(BotEvent.REACH_CURRENT_GENRE_TOP)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CONTINUE_GENRE)
                .target(BotState.CURRENT_YEAR_TOP)
                .event(BotEvent.REACH_CURRENT_YEAR_TOP)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CONTINUE_GENRE)
                .target(BotState.CURRENT_GENRE_PARAM)
                .event(BotEvent.REACH_CURRENT_GENRE_PARAM)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CONTINUE_GENRE)
                .target(BotState.CURRENT_YEAR_PARAM)
                .event(BotEvent.REACH_CURRENT_YEAR_PARAM)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CONTINUE_GENRE)
                .target(BotState.CURRENT_RATING_PARAM)
                .event(BotEvent.REACH_CURRENT_RATING_PARAM)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CONTINUE_GENRE)
                .target(BotState.CURRENT_ACTOR_PARAM)
                .event(BotEvent.REACH_CURRENT_ACTOR_PARAM)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CONTINUE_YEAR)
                .target(BotState.CURRENT_GENRE_TOP)
                .event(BotEvent.REACH_CURRENT_GENRE_TOP)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CONTINUE_YEAR)
                .target(BotState.CURRENT_YEAR_TOP)
                .event(BotEvent.REACH_CURRENT_YEAR_TOP)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CONTINUE_YEAR)
                .target(BotState.CURRENT_GENRE_PARAM)
                .event(BotEvent.REACH_CURRENT_GENRE_PARAM)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CONTINUE_YEAR)
                .target(BotState.CURRENT_YEAR_PARAM)
                .event(BotEvent.REACH_CURRENT_YEAR_PARAM)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CONTINUE_YEAR)
                .target(BotState.CURRENT_RATING_PARAM)
                .event(BotEvent.REACH_CURRENT_RATING_PARAM)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CONTINUE_YEAR)
                .target(BotState.CURRENT_ACTOR_PARAM)
                .event(BotEvent.REACH_CURRENT_ACTOR_PARAM)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CONTINUE_RATING)
                .target(BotState.CURRENT_GENRE_TOP)
                .event(BotEvent.REACH_CURRENT_GENRE_TOP)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CONTINUE_RATING)
                .target(BotState.CURRENT_YEAR_TOP)
                .event(BotEvent.REACH_CURRENT_YEAR_TOP)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CONTINUE_RATING)
                .target(BotState.CURRENT_GENRE_PARAM)
                .event(BotEvent.REACH_CURRENT_GENRE_PARAM)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CONTINUE_RATING)
                .target(BotState.CURRENT_YEAR_PARAM)
                .event(BotEvent.REACH_CURRENT_YEAR_PARAM)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CONTINUE_RATING)
                .target(BotState.CURRENT_RATING_PARAM)
                .event(BotEvent.REACH_CURRENT_RATING_PARAM)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CONTINUE_RATING)
                .target(BotState.CURRENT_ACTOR_PARAM)
                .event(BotEvent.REACH_CURRENT_ACTOR_PARAM)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CONTINUE_PARAM)
                .target(BotState.CURRENT_GENRE_TOP)
                .event(BotEvent.REACH_CURRENT_GENRE_TOP)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CONTINUE_PARAM)
                .target(BotState.CURRENT_YEAR_TOP)
                .event(BotEvent.REACH_CURRENT_YEAR_TOP)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CONTINUE_PARAM)
                .target(BotState.CURRENT_GENRE_PARAM)
                .event(BotEvent.REACH_CURRENT_GENRE_PARAM)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CONTINUE_PARAM)
                .target(BotState.CURRENT_YEAR_PARAM)
                .event(BotEvent.REACH_CURRENT_YEAR_PARAM)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CONTINUE_PARAM)
                .target(BotState.CURRENT_RATING_PARAM)
                .event(BotEvent.REACH_CURRENT_RATING_PARAM)
                .action(reachStateAction())

                .and()
                .withExternal()
                .source(BotState.CONTINUE_PARAM)
                .target(BotState.CURRENT_ACTOR_PARAM)
                .event(BotEvent.REACH_CURRENT_ACTOR_PARAM)
                .action(reachStateAction());
    }
}
