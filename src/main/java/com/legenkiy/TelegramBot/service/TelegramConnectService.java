package com.legenkiy.TelegramBot.service;


import com.legenkiy.TelegramBot.config.properties.BotProperties;
import com.legenkiy.TelegramBot.controller.MessageController;
import com.legenkiy.TelegramBot.data.Actions;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.common.value.qual.IntRange;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.Serializable;


@Component
@Slf4j
@AllArgsConstructor
public class TelegramConnectService extends TelegramLongPollingBot {
    private final BotProperties prop;
    private final MessageController messageController;
    private final WeatherService weatherService;
    private final RespondMessageService respondMessageService;

    @Override
    public String getBotUsername() {
        return prop.getBotName();
    }

    @Override
    public String getBotToken() {
        return prop.getBotToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            Actions actions = Actions.valueOf(callbackQuery.getData());
            long chatId = callbackQuery.getMessage().getChatId();

            switch (actions) {
                case WEATHER_DAYS_3 -> showWeatherForDays(3, chatId);
                case WEATHER_DAYS_5 -> showWeatherForDays(5, chatId);
                case WEATHER -> tryExecute(respondMessageService.print("Введіть /wt НАЗВА МІСТА", chatId));
            }
        } else {
            tryExecute(messageController.messageReceiver(update));
        }

    }

    private void showWeatherForDays(@IntRange(from = 1, to = 16) int quantity, long chatId) {
        String errorMessage = "Дані для цього міста застаріли, або введені некоректно!\n" +
                "введіть /wt та коректну назву міста.";
        String text = String.format("Отримую погоду на %s дні... \uD83D\uDD4A", quantity);
        tryExecute(respondMessageService.print(text, chatId));
        for (int i = 0; i < quantity; i++) {
            if (weatherService.getWeatherForDays(i).equals(errorMessage)) {
                tryExecute(respondMessageService.print(errorMessage, chatId));
                break;
            }
            tryExecute(respondMessageService.print(weatherService.getWeatherForDays(i), chatId));
        }
    }

    private <T extends Serializable, Method extends BotApiMethod<T>> void tryExecute(Method method) {
        try {
            execute(method);
        } catch (TelegramApiException e) {
            log.error("При обробці оновлення виникла помилка", e);
        }
    }

}
