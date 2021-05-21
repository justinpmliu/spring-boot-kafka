package com.example.springbootkafka.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class KafkaConsumer {

    @KafkaListener(topics = "app.test", containerFactory = "batchFactory")
    public void listen(List<ConsumerRecord<String, String>> records, Consumer consumer) {
        if (!records.isEmpty()) {
            log.info("record count: {}", records.size());
            try {
                this.doProcess(records);
            } catch (Exception e) {
                log.error("Batch consumer threw an exception", e);
                this.logErrorBatch(records);
            } finally {
                consumer.commitSync();
            }
        }
    }

    private void doProcess(List<ConsumerRecord<String, String>> records) throws Exception {
        for (ConsumerRecord<String, String> record : records) {
            log.info("partition: {}, offset: {}, key: {}, value: {}",
                    record.partition(), record.offset(), record.key(), record.value());

            if (record.value().contains("12")) {
                throw new Exception("test");
            }
        }

    }

    private void logErrorBatch(List<ConsumerRecord<String, String>> records) {
        List<String> keys = new ArrayList<>();
        for (ConsumerRecord<String, String> record : records) {
            keys.add(record.key());
        }
        log.error("{} records with errors, keys: [{}]", records.size(), String.join(", ", keys));
    }
}
