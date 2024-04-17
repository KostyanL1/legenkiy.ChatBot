package com.legenkiy.TelegramBot.config;

import com.legenkiy.TelegramBot.service.TelegramConnectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Slf4j
@Configuration
public class BotConfiguration {

    private final TelegramConnectService telegramBotController;
    @Autowired
    public BotConfiguration(TelegramConnectService telegramBotController) {
        this.telegramBotController = telegramBotController;
    }
    @EventListener(ContextRefreshedEvent.class)
    public void init() {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi((DefaultBotSession.class));
            telegramBotsApi.registerBot(telegramBotController);
        }catch (TelegramApiException e){
            log.error("Проблема з ініцалізацією боту!");
        }

    }
}
