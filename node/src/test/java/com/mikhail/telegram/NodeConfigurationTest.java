package com.mikhail.telegram;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;

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
    void testSaltPhoto() {
        if ("".equals(saltPhoto)) {
            fail("saltPhoto can't be empty!");
        }
    }

    @Test
    void testSaltDoc() {
        if ("".equals(saltDoc)) {
            fail("saltDoc can't be empty!");
        }
    }

    @Test
    void testSaltUserId() {
        if ("".equals(saltUserId)) {
            fail("saltUserId can't be empty!");
        }
    }
}
