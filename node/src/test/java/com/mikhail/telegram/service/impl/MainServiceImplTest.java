package com.mikhail.telegram.service.impl;

import com.mikhail.telegram.dao.RawDataDAO;
import com.mikhail.telegram.entity.RawData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashSet;
import java.util.Set;

@SpringBootTest
public class MainServiceImplTest {

    @Autowired
    private RawDataDAO rawDataDAO;

    @Test
    public void testSaveRawDAOIntegration() {
        Update update = new Update();
        Message message = new Message();
        message.setText("test");
        update.setMessage(message);

        RawData rawData = RawData.builder()
                .event(update)
                .build();

        Set<RawData> testSet = new HashSet<>();
        testSet.add(rawData);

        rawDataDAO.save(rawData);

        Assert.isTrue(testSet.contains(rawData), "Raw data is not find in testSet");
    }
}
