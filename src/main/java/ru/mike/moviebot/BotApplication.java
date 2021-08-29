package ru.mike.moviebot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.mike.moviebot.config.property.SystemBotPropertyConfig;
import ru.mike.moviebot.handler.IButtonHandler;
import ru.mike.moviebot.service.common.TelegramService;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Component
public class BotApplication extends TelegramLongPollingBot {
    //TODO: heroku ps:scale web=1 для запуска на хероку
    @Autowired
    private List<IButtonHandler> buttonHandlers;
    @Autowired
    private SystemBotPropertyConfig sysBot;
    ExecutorService executorService = Executors.newCachedThreadPool();

    @PostConstruct
    public void registerBot() {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(this);
            log.info("Trying to register bot...");
        } catch (TelegramApiException e) {
            log.error("Can't register bot!", e);
        }
        log.info("Bot was successfully registered");
    }

    @Override
    public String getBotUsername() {
        return sysBot.getName();
    }

    @Override
    public String getBotToken() {
        return sysBot.getTelegramToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        iterateHandlersAsync(update);
    }

    private void iterateHandlersAsync(Update update) {
        executorService.execute(() -> {
            String userText;

            if (update == null || TelegramService.getMessage(update) == null) {
                log.warn("update is null!");
                return;
            }

            if (!update.hasCallbackQuery()) {
                userText = TelegramService.getMessage(update).getText().toLowerCase().trim();
            }
            else {
                userText = update.getCallbackQuery().getData();
            }

            for (IButtonHandler handler : buttonHandlers) {
                if (
                        handler.getMessage().equals(userText) ||
                                handler.getMessage(update, userText).equals(userText)) {
                    handler.execute(BotApplication.this, update);
                }
            }
        });
    }
}
