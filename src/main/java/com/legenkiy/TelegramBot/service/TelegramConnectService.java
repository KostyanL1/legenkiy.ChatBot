package com.legenkiy.TelegramBot.service;


import com.legenkiy.TelegramBot.config.properties.BotProperties;
import com.legenkiy.TelegramBot.controller.MessageController;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.swing.text.html.HTML;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
@AllArgsConstructor
public class TelegramConnectService extends TelegramLongPollingBot {
    private final BotProperties prop;
    private final MessageController messageController;
    private final WeatherService weatherService;

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
            if (update.hasCallbackQuery()) {
                CallbackQuery callbackQuery = update.getCallbackQuery();
                String callBackData = callbackQuery.getData();
                if (callBackData.equals("weather_button")) {
                    var message = new SendMessage();
                    message.setChatId(String.valueOf(callbackQuery.getMessage().getChatId()));
                    message.setText("Введіть [  /wt НАЗВА МІСТА  ]:" );
                    message.setParseMode(ParseMode.HTML);
                    execute(message);
                }
            }
            else {
                SendMessage sendMessage = messageController.messageReceiver(update);
                execute(sendMessage);
            }
        } catch (TelegramApiException e) {
            log.error("При обробці оновлення виникла помилка", e);
        }


    }
}
