package com.example.springbootkafka.producer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class KafkaProducerTest {
    @Autowired
    private KafkaProducer kafkaProducer;

    @Test
    public void testSend() {
        kafkaProducer.sendMessage("test", "first_topic");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}