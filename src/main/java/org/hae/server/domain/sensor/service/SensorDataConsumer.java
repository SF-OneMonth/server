package org.hae.server.domain.sensor.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hae.server.domain.alarm.model.AlarmSetting;
import org.hae.server.domain.alarm.mapper.AlarmSettingMapper;
import org.hae.server.domain.alarm.service.AlarmService;
import org.hae.server.domain.sensor.model.SensorData;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;

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
    @Transactional
    public void consume(String message) {
        try {
            log.debug("센서 데이터 수신: {}", message);
            SensorData sensorData = objectMapper.readValue(message, SensorData.class);

            // 비동기 처리 시작
            processSensorDataAsync(sensorData)
                    .exceptionally(throwable -> {
                        log.error("센서 데이터 처리 중 오류 발생: {}", throwable.getMessage());
                        return null;
                    });

            log.info("센서 데이터 처리 시작됨: equipmentCode={}, sensorType={}",
                    sensorData.getEquipmentCode(), sensorData.getSensorType());

        } catch (JsonProcessingException e) {
            log.error("센서 데이터 파싱 오류: {}", message, e);
        } catch (Exception e) {
            log.error("예상치 못한 오류 발생: {}", message, e);
        }
    }

    @Async("sensorTaskExecutor")
    public CompletableFuture<Void> processSensorDataAsync(SensorData sensorData) {
        // MongoDB 저장 비동기 처리
        CompletableFuture<Void> mongoSave = CompletableFuture.runAsync(() -> {
            try {
                mongoTemplate.save(sensorData);
                log.debug("센서 데이터 MongoDB 저장 완료: {}", sensorData);
            } catch (DataAccessException e) {
                log.error("MongoDB 저장 실패: {}", sensorData, e);
                throw e;
            }
        });

        // WebSocket 전송 비동기 처리
        CompletableFuture<Void> websocketSend = CompletableFuture.runAsync(() -> {
            try {
                webSocketService.sendRealtimeSensorData(sensorData);
                log.debug("센서 데이터 WebSocket 전송 완료: {}", sensorData);
            } catch (Exception e) {
                log.error("WebSocket 전송 실패: {}", sensorData, e);
                throw e;
            }
        });

        // 알람 체크 비동기 처리
        CompletableFuture<Void> alarmCheck = CompletableFuture.runAsync(() -> {
            try {
                alarmService.processAlarm(sensorData);
                log.debug("알람 체크 완료: {}", sensorData);
            } catch (Exception e) {
                log.error("알람 처리 실패: {}", sensorData, e);
                throw e;
            }
        });

        // 모든 비동기 작업 완료 대기 및 결과 처리
        return CompletableFuture.allOf(mongoSave, websocketSend, alarmCheck)
                .thenRun(() -> {
                    log.info("모든 센서 데이터 처리 완료: equipmentCode={}, sensorType={}",
                            sensorData.getEquipmentCode(), sensorData.getSensorType());
                });
    }
}