package com.legenkiy.TelegramBot.service;


import com.legenkiy.TelegramBot.config.properties.BotProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@Slf4j
public class TelegramBotConnect extends TelegramLongPollingBot {
    private final BotProperties prop;
    private final MessageService messageService;
    @Autowired
    public TelegramBotConnect(BotProperties prop, MessageService messageService) {
        this.prop = prop;
        this.messageService = messageService;
    }

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
            SendMessage sendMessage = messageService.messageReceiver(update);
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("При ответе пользователю возникла проблема");
        }
    }
}
