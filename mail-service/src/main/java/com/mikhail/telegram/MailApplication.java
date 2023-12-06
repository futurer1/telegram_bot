package com.mikhail.telegram;

import lombok.extern.log4j.Log4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:application.properties")
@Log4j
public class MailApplication {
    public static void main(String[] args) {
        SpringApplication.run(MailApplication.class, args);
        log.debug("Mail service is start");
    }
}
