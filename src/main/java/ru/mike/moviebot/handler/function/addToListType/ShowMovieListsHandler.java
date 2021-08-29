package ru.mike.moviebot.handler.function.addToListType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.mike.moviebot.BotApplication;
import ru.mike.moviebot.api.telegram.sending.TelegramSending;
import ru.mike.moviebot.handler.IButtonHandler;
import ru.mike.moviebot.keyboard.KeyboardUtils;
import ru.mike.moviebot.service.common.TelegramService;
import ru.mike.moviebot.service.database.UserService;
import ru.mike.moviebot.service.statemachineService.BotStateMachineService;

import java.util.Optional;

@Component
public class ShowMovieListsHandler implements TelegramSending, IButtonHandler {
    @Autowired
    KeyboardUtils keyboardUtils;
    @Autowired
    UserService userService;
    @Autowired
    BotStateMachineService stateMachineService;

    @Override
    public String getMessage() {
        return "597a8769-8fdd-42de-8d88-3f2a8c3e45ed";
    }

    /**
     * Показывает пользователю его доступные списки или выводит сообщение об их отсутствии
     *
     * @param botApplication бот
     * @param update ответ пользователя на нажатие кнопки добавления фильма в новый список
     */
    @Override
    public void execute(BotApplication botApplication, Update update) {
        Optional<InlineKeyboardMarkup> inlineKeyboardMarkup = keyboardUtils
                .addingToListKeyboard().setInlineShowAvailableMovieListsButtons(update);

        /*
         * некоторые фильмы могут быть без постера, поэтому проверяем, есть ли к нему описание, если его нет -
         * постера тоже нет. Тогда шлем EditMessageText, иначе с EditMessageCaption будет exception
         * при нажатии на кнопку "добавить в список"
         */
        if (TelegramService.getMessage(update).getCaption() == null) {
            EditMessageText editMessageText = new EditMessageText();
            editMessageText
                    .setChatId(TelegramService.getMessage(update).getChatId().toString())
                    .setMessageId(TelegramService.getMessage(update).getMessageId())
                    .setText(TelegramService.getMessage(update).getText());

            if (inlineKeyboardMarkup.isPresent()) {
                editMessageText.setReplyMarkup(inlineKeyboardMarkup.get());
            } else {
                editMessageText.setReplyMarkup(keyboardUtils.commonKeyboards().setEmptyKeyboard());
            }
            sendBotEditMessage(botApplication, editMessageText);

        } else {
            EditMessageCaption editMessageCaption = new EditMessageCaption();
            editMessageCaption
                    .setChatId(TelegramService.getMessage(update).getChatId().toString())
                    .setMessageId(TelegramService.getMessage(update).getMessageId())
                    .setCaption(TelegramService.getMessage(update).getCaption());

            if (inlineKeyboardMarkup.isPresent()) {
                editMessageCaption.setReplyMarkup(inlineKeyboardMarkup.get());
            } else {
                editMessageCaption.setReplyMarkup(keyboardUtils.commonKeyboards().setEmptyKeyboard());
            }
            sendBotEditCaption(botApplication, editMessageCaption);
        }
    }
}
