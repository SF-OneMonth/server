package org.hae.server.domain.sensor.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hae.server.domain.alarm.model.AlarmSetting;
import org.hae.server.domain.alarm.mapper.AlarmSettingMapper;
import org.hae.server.domain.alarm.service.AlarmService;
import org.hae.server.domain.sensor.model.SensorData;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SensorDataConsumer {

    private final MongoTemplate mongoTemplate;
    private final ObjectMapper objectMapper;
    private final SimpMessagingTemplate messagingTemplate;
    private final AlarmService alarmService;
    private final AlarmSettingMapper alarmSettingMapper;

    @KafkaListener(topics = "sensor-data", groupId = "sensor-group")
    public void consume(String message) {
        try {
            SensorData sensorData = objectMapper.readValue(message, SensorData.class);

            // MongoDB에 저장
            mongoTemplate.save(sensorData);

            // WebSocket으로 실시간 전송
            String destination = "/topic/sensor/" + sensorData.getEquipmentCode();
            messagingTemplate.convertAndSend(destination, sensorData);

            // 알람 체크
            AlarmSetting alarmSetting = alarmSettingMapper.findByEquipmentCodeAndSensorType(
                    sensorData.getEquipmentCode(),
                    sensorData.getSensorType()).orElse(null);

            if (alarmSetting != null) {
                alarmService.checkAndCreateAlarm(sensorData, alarmSetting);
            }

            log.info("Sensor data processed: {}", sensorData);
        } catch (Exception e) {
            log.error("Error processing sensor data: {}", message, e);
        }
    }
}