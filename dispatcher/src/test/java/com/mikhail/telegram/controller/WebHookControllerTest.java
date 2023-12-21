package com.mikhail.telegram.controller;

import com.mikhail.telegram.config.RabbitConfiguration;
import com.mikhail.telegram.service.UpdateProducer;
import static com.mikhail.telegram.controller.TestUpdates.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.net.URI;
import java.net.URISyntaxException;


import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
public class WebHookControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int randomServerPort;

    @MockBean
    private UpdateProducer updateProducer;

    @Value("${spring.rabbitmq.queues.text-message-update}")
    private String queueTextMessageUpdate;

    @Value("${spring.rabbitmq.queues.doc-message-update}")
    private String queueDocMessageUpdate;

    @Value("${spring.rabbitmq.queues.photo-message-update}")
    private String queuePhotoMessageUpdate;

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Cache-Control", "no-cache");
        return headers;
    }

    private URI getUri() throws URISyntaxException {
        final String baseUrl = "http://localhost:" + randomServerPort + "/";
        return new URI(baseUrl);
    }

    @Test
    void onUpdateReceivedMessageWithText() throws URISyntaxException {
        HttpEntity<String> request = new HttpEntity<>(MESSAGE_WITH_TEXT, getHeaders());
        ResponseEntity<String> result = this.restTemplate.postForEntity(
                getUri() + "callback/update",
                request,
                String.class
        );
        assertEquals(HttpStatus.OK, result.getStatusCode());

        Mockito.verify(updateProducer, Mockito.times(1))
                .produce(eq(queueTextMessageUpdate),
                        any(Update.class));
    }

    @Test
    void onUpdateReceivedMessageWithDocument() throws URISyntaxException {
        HttpEntity<String> request = new HttpEntity<>(MESSAGE_WITH_DOCUMENT, getHeaders());
        ResponseEntity<String> result = this.restTemplate.postForEntity(
                getUri() + "callback/update",
                request,
                String.class
        );
        assertEquals(HttpStatus.OK, result.getStatusCode());

        Mockito.verify(updateProducer, Mockito.times(1))
                .produce(eq(queueDocMessageUpdate),
                        any(Update.class));
    }

    @Test
    void onUpdateReceivedMessageWithPhoto() throws URISyntaxException {

        //System.out.println(MESSAGE_WITH_PHOTO);

        HttpEntity<String> request = new HttpEntity<>(MESSAGE_WITH_PHOTO, getHeaders());
        ResponseEntity<String> result = this.restTemplate.postForEntity(
                getUri() + "callback/update",
                request,
                String.class
        );
        assertEquals(HttpStatus.OK, result.getStatusCode());

        Mockito.verify(updateProducer, Mockito.times(1))
                .produce(eq(queuePhotoMessageUpdate),
                        any(Update.class));
    }
}
