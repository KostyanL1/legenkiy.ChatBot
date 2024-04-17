package com.legenkiy.TelegramBot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class MessageService {
    private final WeatherService weatherService;
    @Autowired
    public MessageService(WeatherService weatherService) {
        this.weatherService = weatherService;
    }


    public SendMessage messageReceiver(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()){
            var text = update.getMessage().getText();
            var chatId = update.getMessage().getChatId();
            var name = update.getMessage().getChat().getFirstName();

            String responseText;
            if (text.startsWith("/weather")) {
                // Розділити введення на слова
                String[] words = text.split(" ");
                if (words.length >= 2) {
                    // Отримати друге слово як назву міста
                    String cityName = words[1];
                    // Отримати погоду за вказаним містом
                    String result = weatherService.getWeatherByLocation(cityName).toString();
                    responseText = result;


                } else {
                    responseText = "Невірний формат команди. Використовуйте /weather [назва міста].";
                }
            } else {
                // Обробка інших команд або повідомлень
                switch (text){
                    case "/start" -> responseText = String.format(
                            "Hello, " + name +  "! I'm a bot that can help you on your travels.\n" +
                                    "Any ideas where we can start?");
                    case "/stop" -> responseText = String.format(
                            "Great, hope I was able to help you.\n" +
                                    "If you need anything, you know where to find me. \ud83d\ude09");
                    default -> responseText = String.format("I don`t know this command. Try again.");
                }
            }

            var message = new SendMessage();
            message.setChatId(String.valueOf(chatId));
            message.setText(responseText);
            return message;
        }
        return null;
    }
}
