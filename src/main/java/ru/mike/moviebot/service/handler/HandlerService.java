package ru.mike.moviebot.service.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mike.moviebot.config.property.CommandsPropertyConfig;

import java.util.ArrayList;
import java.util.List;

@Service
public final class HandlerService {
    @Autowired
    private CommandsPropertyConfig commandsBot;

    public List<String> getMainMenuButtonNames() {
        List<String> mainMenuButtonNames = new ArrayList<>();
        mainMenuButtonNames.add(commandsBot.getMainMenu().getRandom_movie());
        mainMenuButtonNames.add(commandsBot.getMainMenu().getTop_movie());
        mainMenuButtonNames.add(commandsBot.getMainMenu().getParam_movie());
        mainMenuButtonNames.add(commandsBot.getMainMenu().getList_movie());
        return new ArrayList<>(mainMenuButtonNames);
    }
}
