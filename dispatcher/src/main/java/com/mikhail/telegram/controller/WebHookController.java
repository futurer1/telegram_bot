package com.mikhail.telegram.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;

@RestController
@Log4j
public class WebHookController {

    private final UpdateProcessor updateProcessor;

    private final Environment env;

    public WebHookController(UpdateProcessor updateProcessor, Environment env) {
        this.updateProcessor = updateProcessor;
        this.env = env;

        boolean devProfile = Arrays.asList(env.getActiveProfiles()).contains("dev");
        log.debug("dev profile is activated: " + devProfile);
    }

    @PostMapping("/callback/update")
    public ResponseEntity<?> onUpdateReceived(@RequestBody Update update) {

        updateProcessor.processUpdate(update);
        return ResponseEntity.ok().build();
    }
}
