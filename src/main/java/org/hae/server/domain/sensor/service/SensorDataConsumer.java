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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Kafka를 통해 수신된 센서 데이터를 처리하는 컨슈머 서비스
 * 데이터 저장, 실시간 전송, 알람 처리를 담당합니다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SensorDataConsumer {
    private final MongoTemplate mongoTemplate;
    private final ObjectMapper objectMapper;
    private final SensorWebSocketService webSocketService;
    private final AlarmService alarmService;
    private final AlarmSettingMapper alarmSettingMapper;

    /**
     * Kafka 토픽으로부터 센서 데이터를 수신하고 처리하는 메서드
     * 
     * @param message JSON 형식의 센서 데이터 메시지
     */
    @KafkaListener(topics = "sensor-data", groupId = "sensor-group")
    @Transactional
    public void consume(String message) {
        try {
            log.debug("센서 데이터 수신: {}", message);
            SensorData sensorData = objectMapper.readValue(message, SensorData.class);
            processSensorData(sensorData);
            log.info("센서 데이터 처리 완료: equipmentCode={}, sensorType={}",
                    sensorData.getEquipmentCode(), sensorData.getSensorType());
        } catch (Exception e) {
            log.error("센서 데이터 처리 중 오류 발생: {}", message, e);
        }
    }

    /**
     * 수신된 센서 데이터를 처리하는 내부 메서드
     * MongoDB 저장, WebSocket 전송, 알람 처리를 수행합니다.
     * 
     * @param sensorData 처리할 센서 데이터
     */
    private void processSensorData(SensorData sensorData) {
        // MongoDB 저장
        mongoTemplate.save(sensorData);
        log.debug("센서 데이터 MongoDB 저장 완료: {}", sensorData);

        // WebSocket 전송
        webSocketService.sendRealtimeSensorData(sensorData);
        log.debug("센서 데이터 WebSocket 전송 완료: {}", sensorData);

        // 알람 체크
        processAlarm(sensorData);
    }

    /**
     * 센서 데이터에 대한 알람 조건을 확인하고 처리하는 메서드
     * 
     * @param sensorData 알람 체크할 센서 데이터
     */
    private void processAlarm(SensorData sensorData) {
        AlarmSetting alarmSetting = alarmSettingMapper
                .findByEquipmentCodeAndSensorType(
                        sensorData.getEquipmentCode(),
                        sensorData.getSensorType())
                .orElse(null);

        if (alarmSetting != null) {
            log.debug("알람 설정 확인: equipmentCode={}, sensorType={}",
                    sensorData.getEquipmentCode(), sensorData.getSensorType());
            alarmService.checkAndCreateAlarm(sensorData, alarmSetting);
        }
    }
}