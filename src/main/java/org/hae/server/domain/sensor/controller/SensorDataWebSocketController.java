package org.hae.server.domain.sensor.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hae.server.domain.sensor.model.SensorData;
import org.hae.server.domain.sensor.service.SensorWebSocketService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

/**
 * WebSocket을 통한 실시간 센서 데이터 통신을 처리하는 컨트롤러
 * STOMP 프로토콜을 사용하여 메시지를 라우팅하고 처리합니다.
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class SensorDataWebSocketController {
    private final SensorWebSocketService webSocketService;

    /**
     * 특정 장비의 센서 데이터를 WebSocket을 통해 수신하고 브로드캐스트하는 메서드
     * 
     * @param equipmentCode 장비 코드 (URL 경로 변수)
     * @param sensorData    수신된 센서 데이터
     * @return 브로드캐스트할 센서 데이터
     */
    @MessageMapping("/sensor/{equipmentCode}")
    @SendTo("/topic/sensor/{equipmentCode}")
    public SensorData sendSensorData(
            @DestinationVariable String equipmentCode,
            SensorData sensorData) {
        try {
            log.debug("WebSocket 메시지 수신: equipmentCode={}, data={}", equipmentCode, sensorData);
            webSocketService.sendRealtimeSensorData(sensorData);
            log.debug("WebSocket 메시지 브로드캐스트 완료: equipmentCode={}", equipmentCode);
            return sensorData;
        } catch (Exception e) {
            log.error("WebSocket 메시지 처리 중 오류 발생: equipmentCode={}, data={}", equipmentCode, sensorData, e);
            throw e;
        }
    }
}