package org.hae.server.domain.sensor.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hae.server.domain.alarm.model.AlarmHistory;
import org.hae.server.domain.sensor.exception.WebSocketTransmissionException;
import org.hae.server.domain.sensor.model.SensorData;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * WebSocket을 통한 실시간 데이터 전송을 담당하는 서비스
 * 센서 데이터와 알람 데이터의 실시간 전송을 처리합니다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SensorWebSocketService {
    private final SimpMessagingTemplate messagingTemplate;

    // WebSocket 토픽 상수 정의
    private static final String SENSOR_TOPIC = "/topic/sensor/";
    private static final String ALARM_TOPIC = "/topic/alarms/";

    /**
     * 실시간 센서 데이터를 WebSocket을 통해 전송
     * 
     * @param sensorData 전송할 센서 데이터
     * @throws WebSocketTransmissionException WebSocket 전송 실패 시
     */
    public void sendRealtimeSensorData(SensorData sensorData) {
        try {
            String destination = SENSOR_TOPIC + sensorData.getEquipmentCode();
            messagingTemplate.convertAndSend(destination, sensorData);
            log.debug("센서 데이터 WebSocket 전송 완료: destination={}, data={}", destination, sensorData);
        } catch (Exception e) {
            log.error("WebSocket 전송 실패: {}", sensorData, e);
            throw new WebSocketTransmissionException(e);
        }
    }

    /**
     * 알람 발생 정보를 WebSocket을 통해 전송
     * 
     * @param equipmentCode 장비 코드
     * @param alarmHistory  알람 이력 정보
     * @throws WebSocketTransmissionException WebSocket 전송 실패 시
     */
    public void sendSensorAlarm(String equipmentCode, AlarmHistory alarmHistory) {
        try {
            String destination = ALARM_TOPIC + equipmentCode;
            messagingTemplate.convertAndSend(destination, alarmHistory);
            log.debug("알람 데이터 WebSocket 전송 완료: destination={}, alarm={}", destination, alarmHistory);
        } catch (Exception e) {
            log.error("알람 WebSocket 전송 실패: equipmentCode={}, alarm={}", equipmentCode, alarmHistory, e);
            throw new WebSocketTransmissionException(e);
        }
    }
}