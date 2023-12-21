package com.mikhail.telegram;

import lombok.extern.log4j.Log4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@Log4j
@PropertySource("classpath:application.properties")
@PropertySource("classpath:bot.properties")
@SpringBootApplication
public class DispatcherApplication {

    public static void main(String[] args) {
        SpringApplication.run(DispatcherApplication.class, args);
        log.debug("Dispatcher is start");
    }
}
