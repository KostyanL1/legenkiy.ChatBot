package com.legenkiy.TelegramBot.config.properties;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class BotProperties {
    @Value("${bot.name}")
    private String botName;
    @Value("${bot.token}")
    private String botToken;
    @Value("${api.key}")
    private String apiKey;
    @Value("${api.url}")
    private String apiUrl;
    @Value("${text.start}")
    String startText;
}
