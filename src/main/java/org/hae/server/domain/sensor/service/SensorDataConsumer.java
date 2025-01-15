package org.hae.server.domain.sensor.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hae.server.domain.alarm.service.AlarmService;
import org.hae.server.domain.sensor.model.SensorData;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * Kafka를 통해 수신된 센서 데이터를 처리하는 컨슈머 서비스
 * 데이터 저장, 실시간 전송, 알람 처리를 담당합니다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SensorDataConsumer {
    private final MongoTemplate mongoTemplate;
    private final SensorWebSocketService webSocketService;
    private final AlarmService alarmService;
    private final ObjectMapper objectMapper;

    /**
     * Kafka 토픽으로부터 센서 데이터를 수신하고 처리하는 메서드
     * 
     * @param message JSON 형식의 센서 데이터 메시지
     */
    @KafkaListener(topics = "sensor-data", groupId = "sensor-group")
    public void consume(String message) {
        try {
            log.debug("센서 데이터 수신: {}", message);
            SensorData sensorData = objectMapper.readValue(message, SensorData.class);

            // 1. MongoDB 저장
            mongoTemplate.save(sensorData);
            log.debug("센서 데이터 MongoDB 저장 완료: {}", sensorData);

            // 2. WebSocket으로 실시간 데이터 전송
            webSocketService.sendRealtimeSensorData(sensorData);

            // 3. 알람 처리
            alarmService.processAlarm(sensorData);

            log.info("센서 데이터 처리 완료: equipmentCode={}, sensorType={}",
                    sensorData.getEquipmentCode(), sensorData.getSensorType());

        } catch (JsonProcessingException e) {
            log.error("센서 데이터 파싱 오류: {}", message, e);
        } catch (Exception e) {
            log.error("예상치 못한 오류 발생: {}", message, e);
        }
    }
}