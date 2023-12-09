package com.mikhail.telegram.controller;

import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.objects.Update;

@RequiredArgsConstructor
@RestController
public class WebHookController {

    private final UpdateProcessor updateProcessor;

    @PostMapping("/callback/update")
    public ResponseEntity<?> onUpdateReceived(@RequestBody Update update) {

        updateProcessor.processUpdate(update);
        return ResponseEntity.ok().build();
    }
}
