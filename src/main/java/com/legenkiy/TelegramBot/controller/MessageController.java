package com.legenkiy.TelegramBot.controller;

import com.legenkiy.TelegramBot.service.RespondMessageService;
import com.legenkiy.TelegramBot.service.WeatherService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Service
@AllArgsConstructor
public class MessageController {
    private final WeatherService weatherService;
    private final RespondMessageService respondMessageService;

    public SendMessage messageReceiver(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {

            var text = update.getMessage().getText();
            var chatId = update.getMessage().getChatId();
            var name = update.getMessage().getChat().getFirstName();
            String responseText;


            if (text.startsWith("/wt")) {
                // Розділити введення на слова
                String[] words = text.split(" ");
                if (words.length >= 2) {
                    // Отримати друге слово як назву міста
                    String cityName = words[1];
                    // Отримати погоду за вказаним містом
                    weatherService.getWeatherByLocationDayOne(cityName);

                    var message = new SendMessage();
                    message.setChatId(String.valueOf(chatId));
                    message.setText(weatherService.getWeatherForDays(0));

                    //створення кнопок та їх оформлення
                    InlineKeyboardMarkup inlineKeyboardMarkup = respondMessageService.getInlineKeyboardMarkup();
                    message.setReplyMarkup(inlineKeyboardMarkup);
                    return message;
                } else {

                    responseText = "Невірний формат команди. Використовуйте /wt назва міста.";
                    var message = new SendMessage();
                    message.setChatId(String.valueOf(chatId));
                    message.setText(responseText);
                    return message;

                }
            } else {
                var message = new SendMessage();
                switch (text) {
                    case "/start" -> {
                        message = respondMessageService.getStartMessage(chatId, name);
                        return message;
                    }
                    case "/stop" -> {
                        message.setChatId(String.valueOf(chatId));
                        message.setText(
                                "Дякую, що скористались моєю допомогою для отримання інформації про погоду\n" +
                                        "Якщо потрібна буде допомога, звертайтесь!");
                        return message;
                    }
                    default -> {
                        message.setChatId(String.valueOf(chatId));
                        message.setText(
                                "Вибачте, але я не знаю такої команди. \n" +
                                        "Перевірте, які команди я можу використовуватив меню!");

                        return message;
                    }
                }
            }
        }
        return null;
    }


}
