package com.mikhail.telegram.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.Random;

public class CryptoToolTest {

    @Test
    @RepeatedTest(10)
    void hashOfEqualsIdOf() {
        var testSalt = "testsalt";

        CryptoTool cryptoTool = new CryptoTool(testSalt);
        var rand = new Random();
        Long id = rand.nextLong(9007199254740992L);

        var hash = cryptoTool.hashOf(id);
        var idFromHash = cryptoTool.idOf(hash);

        Assertions.assertEquals(idFromHash, id);
    }
}
