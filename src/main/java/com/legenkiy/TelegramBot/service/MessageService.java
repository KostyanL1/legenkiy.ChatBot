package com.legenkiy.TelegramBot.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class MessageService {

    public SendMessage messageReceiver(Update update){
        if (update.hasMessage() && update.getMessage().hasText()){
            var text = update.getMessage().getText();
            var chatId = update.getMessage().getChatId();
            var name = update.getMessage().getChat().getFirstName();

            String responseText;
            switch (text){
                case "/start" -> responseText = String.format(
                        "Hello, " + name +  "! I'm a bot that can help you on your travels.\n" +
                        "Any ideas where we can start?");
                case "/stop" -> responseText = String.format(
                        "Great, hope I was able to help you.\n" +
                        "If you need anything, you know where to find me. \ud83d\ude09");
                default -> responseText = String.format("Ти єблан ? я такого не понімаю ");
            }

            var message = new SendMessage();
            message.setChatId(String.valueOf(chatId));
            message.setText(responseText);
            return message;
        }
        return null;
    }
}
