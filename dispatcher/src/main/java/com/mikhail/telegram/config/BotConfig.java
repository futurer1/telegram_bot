package com.mikhail.telegram.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:application.yml")
@Data
@Configuration
public class BotConfig {
    @Value("${bot.name}")
    String botName;

    @Value("${bot.token}")
    String botToken;

    @Value("${bot.owner}")
    Long botOwner;

    @Value("${bot.uri}")
    String botUri;

    @Value("${bot.muteanswer}")
    Boolean botMuteAnswer;
}
