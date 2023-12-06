package com.mikhail.telegram.controller;

import com.mikhail.telegram.dto.MailParams;
import com.mikhail.telegram.service.impl.MailSenderImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/mail")
@RestController
public class MailController {

    private final MailSenderImpl mailSenderService;

    public MailController(MailSenderImpl mailSenderService) {
        this.mailSenderService = mailSenderService;
    }

    @PostMapping("/send")
    public ResponseEntity<?> sendActivationMail(@RequestBody MailParams mailParams) {

        mailSenderService.send(mailParams);

        return ResponseEntity.ok().build();
    }
}
