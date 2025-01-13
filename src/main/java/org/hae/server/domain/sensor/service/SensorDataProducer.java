package org.hae.server.domain.sensor.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SensorDataProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendSensorData(String equipmentCode, String message) {
        kafkaTemplate.send("sensor-data", equipmentCode, message)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info("Sent message: {}", message);
                    } else {
                        log.error("Failed to send message", ex);
                    }
                });
    }
}