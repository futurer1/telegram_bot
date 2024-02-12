package com.mikhail.telegram.service.controller;

import com.mikhail.telegram.service.impl.UserActivationServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping("/user")
@RestController
public class ActivationController {

    private final UserActivationServiceImpl userActivationService;

    public ActivationController(UserActivationServiceImpl userActivationService) {
        this.userActivationService = userActivationService;
    }

    @GetMapping("/activation")
    public ResponseEntity<?> activate(@RequestParam("id") String id) {

        boolean result = userActivationService.activate(id);
        if (result) {
            return ResponseEntity.ok()
                    .body("Регистрация завершена успешно.");
        }

        return ResponseEntity.internalServerError().build();

    }
}
