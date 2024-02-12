package com.mikhail.telegram.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
public class NodeConfigurationTest {

    @Value("${salt.photo}")
    private String saltPhoto;

    @Value("${salt.doc}")
    private String saltDoc;

    @Value("${salt.userid}")
    private String saltUserId;

    @Test
    void testSaltPhotoIntegration() {
        if ("".equals(saltPhoto)) {
            fail("saltPhoto can't be empty!");
        }
    }

    @Test
    void testSaltDocIntegration() {
        if ("".equals(saltDoc)) {
            fail("saltDoc can't be empty!");
        }
    }

    @Test
    void testSaltUserIdIntegration() {
        if ("".equals(saltUserId)) {
            fail("saltUserId can't be empty!");
        }
    }
}
