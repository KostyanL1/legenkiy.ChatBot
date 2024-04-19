package com.legenkiy.TelegramBot.service;


import com.legenkiy.TelegramBot.config.properties.BotProperties;
import com.legenkiy.TelegramBot.controller.MessageController;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

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

        try {
            String errorMessage = "Дані для цього міста застаріли, або введені некоректно!\n" +
                    "введіть /wt та коректну назву міста.";
            if (update.hasCallbackQuery()) {
                CallbackQuery callbackQuery = update.getCallbackQuery();
                String callBackData = callbackQuery.getData();
                long chatId = callbackQuery.getMessage().getChatId();
                if (callBackData.equals("weather_button")) {
                    execute(respondMessageService.print("Введіть /wt НАЗВА МІСТА", chatId));
                }
                if (callBackData.equals("weather_button3days")) {
                    execute(respondMessageService.print("Отримую погоду на 3 дні... \uD83D\uDD4A", chatId));
                    for (int i = 0; i < 3; i++) {
                        if (weatherService.getWeatherForDays(i).equals(errorMessage)){
                            execute(respondMessageService.print(errorMessage, chatId));
                            break;
                        }
                        execute(respondMessageService.print(weatherService.getWeatherForDays(i), chatId));
                    }
                }
                if (callBackData.equals("weather_button5days")) {
                    execute(respondMessageService.print("Отримую погоду на 5 днів... \uD83D\uDD4A", chatId));
                    for (int i = 0; i < 5; i++) {
                        if (weatherService.getWeatherForDays(i).equals(errorMessage)){
                            execute(respondMessageService.print(errorMessage, chatId));
                            break;
                        }
                        execute(respondMessageService.print(weatherService.getWeatherForDays(i), chatId));
                    }
                }
            } else {
                SendMessage sendMessage = messageController.messageReceiver(update);
                execute(sendMessage);
            }
        } catch (TelegramApiException e) {
            log.error("При обробці оновлення виникла помилка", e);
        }


    }
}
