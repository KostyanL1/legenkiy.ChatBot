package com.legenkiy.TelegramBot.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Service
public class RespondMessageService {
    public SendMessage getStartMessage(Long chatId, String name) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();
        var weather_button = new InlineKeyboardButton();
        weather_button.setText("Отримати погоду\uD83C\uDF25");
        weather_button.setCallbackData("weather_button");
        rowInLine.add(weather_button);
        rowsInLine.add(rowInLine);
        inlineKeyboardMarkup.setKeyboard(rowsInLine);
        var message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setReplyMarkup(inlineKeyboardMarkup);
        message.setText("Привіт, " + name + ",  щоб отримати інструкцію, нажми кнопку нижче?");
        return message;
    }

    public InlineKeyboardMarkup getInlineKeyboardMarkup() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();
        var weatherButtonForThreeDays = new InlineKeyboardButton();
        var weatherButtonForFiveDays = new InlineKeyboardButton();
        weatherButtonForThreeDays.setText("на 3 дні\uD83C\uDF25");
        weatherButtonForThreeDays.setCallbackData("weather_button3days");
        weatherButtonForFiveDays.setText("на 5 днів\uD83C\uDF25");
        weatherButtonForFiveDays.setCallbackData("weather_button5days");
        rowInLine.add(weatherButtonForThreeDays);
        rowInLine.add(weatherButtonForFiveDays);
        rowsInLine.add(rowInLine);
        inlineKeyboardMarkup.setKeyboard(rowsInLine);
        return inlineKeyboardMarkup;
    }

    public SendMessage print(String responseText, long chatId){
        var message = new SendMessage();
        message.setText(responseText);
        message.setChatId(String.valueOf(chatId));
        return message;
    }
}
