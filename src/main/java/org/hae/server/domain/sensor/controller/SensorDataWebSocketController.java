package org.hae.server.domain.sensor.controller;

import lombok.RequiredArgsConstructor;
import org.hae.server.domain.sensor.model.SensorData;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class SensorDataWebSocketController {

    @MessageMapping("/sensor/{equipmentCode}")
    @SendTo("/topic/sensor/{equipmentCode}")
    public SensorData sendSensorData(@DestinationVariable String equipmentCode, SensorData sensorData) {
        return sensorData;
    }
}