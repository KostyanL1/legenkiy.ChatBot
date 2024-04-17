package com.legenkiy.TelegramBot.controller;

import com.legenkiy.TelegramBot.service.WeatherService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class MessageController {
    private final WeatherService weatherService;
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
                   var message = new SendMessage();
                   message.setChatId(String.valueOf(chatId));
                   message.setText(weatherService.getWeatherByLocation(cityName).toString());
                    InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                    List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
                    List<InlineKeyboardButton> rowInLine = new ArrayList<>();
                    var weather_button = new InlineKeyboardButton();
                    weather_button.setText("на 3 дні\uD83C\uDF25");
                    weather_button.setCallbackData("weather_button3days");
                    rowInLine.add(weather_button);
                    rowsInLine.add(rowInLine);
                    inlineKeyboardMarkup.setKeyboard(rowsInLine);
                    message.setReplyMarkup(inlineKeyboardMarkup);
                    return message;

                } else {
                    responseText = "Невірний формат команди. Використовуйте /wt [назва міста].";
                    var message = new SendMessage();
                    message.setChatId(String.valueOf(chatId));
                    message.setText(responseText);
                    return message;

                }
            }
            // Обробка інших команд або повідомлень
            else {
                switch (text) {
                    case "/start" -> {

                        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
                        List<InlineKeyboardButton> rowInLine = new ArrayList<>();
                        var weather_button = new InlineKeyboardButton();
                        weather_button.setText("ПОГОДУ\uD83C\uDF25");
                        weather_button.setCallbackData("weather_button");
                        rowInLine.add(weather_button);
                        rowsInLine.add(rowInLine);
                        inlineKeyboardMarkup.setKeyboard(rowsInLine);
                        var message = new SendMessage();
                        message.setChatId(String.valueOf(chatId));
                        message.setReplyMarkup(inlineKeyboardMarkup);
                        message.setText("Привіт, що ти хочеш дізнатись?");
                        return message;

                    }
                    case "/stop" -> responseText = String.format(
                            "Great, hope I was able to help you.\n" +
                                    "If you need anything, you know where to find me. \ud83d\ude09");
                    default -> responseText = String.format("I don`t know this command. Try again.");
                }
            }
        }
        return null;
    }
}
