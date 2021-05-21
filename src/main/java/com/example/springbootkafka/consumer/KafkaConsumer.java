package com.example.springbootkafka.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class KafkaConsumer {

    @KafkaListener(topics = "app.test", containerFactory = "batchFactory")
    public void listen(List<ConsumerRecord<String, String>> records, Acknowledgment ack) {
        if (!records.isEmpty()) {
            log.info("record count: {}", records.size());
            try {
                this.doProcess(records);
            } catch (Exception e) {
                log.error("Batch consumer threw an exception", e);
            } finally {
                ack.acknowledge();
            }
        }
    }

    private void doProcess(List<ConsumerRecord<String, String>> records) throws Exception {
        for (ConsumerRecord<String, String> record : records) {
            log.info("partition: {}, offset: {}, key: {}, value: {}",
                    record.partition(), record.offset(), record.key(), record.value());
        }
    }

}
