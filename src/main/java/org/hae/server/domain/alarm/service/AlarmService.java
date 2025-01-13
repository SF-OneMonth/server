package org.hae.server.domain.alarm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hae.server.domain.alarm.mapper.AlarmHistoryMapper;
import org.hae.server.domain.alarm.model.AlarmHistory;
import org.hae.server.domain.alarm.model.AlarmSetting;
import org.hae.server.domain.alarm.dto.response.AlarmStatisticsDto;
import org.hae.server.domain.sensor.model.SensorData;
import org.hae.server.global.common.exception.SFaaSException;
import org.hae.server.global.common.response.ErrorType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlarmService {

    private final AlarmHistoryMapper alarmHistoryMapper;
    private final SimpMessagingTemplate messagingTemplate;

    @Transactional
    public void checkAndCreateAlarm(SensorData sensorData, AlarmSetting alarmSetting) {
        if (!alarmSetting.isEnabled()) {
            return;
        }

        AlarmHistory.AlarmType alarmType = null;
        Double thresholdValue = null;

        if (sensorData.getValue() > alarmSetting.getMaxThreshold()) {
            alarmType = AlarmHistory.AlarmType.HIGH;
            thresholdValue = alarmSetting.getMaxThreshold();
        } else if (sensorData.getValue() < alarmSetting.getMinThreshold()) {
            alarmType = AlarmHistory.AlarmType.LOW;
            thresholdValue = alarmSetting.getMinThreshold();
        }

        if (alarmType != null) {
            AlarmHistory alarmHistory = AlarmHistory.builder()
                    .equipmentCode(sensorData.getEquipmentCode())
                    .sensorType(sensorData.getSensorType())
                    .sensorValue(sensorData.getValue())
                    .thresholdValue(thresholdValue)
                    .alarmType(alarmType)
                    .occurredAt(LocalDateTime.now())
                    .acknowledged(false)
                    .build();

            alarmHistoryMapper.insert(alarmHistory);

            // WebSocket으로 실시간 알람 전송
            String destination = "/topic/alarms/" + sensorData.getEquipmentCode();
            messagingTemplate.convertAndSend(destination, alarmHistory);

            log.info("Alarm created: {}", alarmHistory);
        }
    }

    @Transactional
    public void acknowledgeAlarm(Long id) {
        AlarmHistory alarmHistory = alarmHistoryMapper.findById(id)
                .orElseThrow(() -> new SFaaSException(ErrorType.ALARM_NOT_FOUND));

        if (alarmHistory.isAcknowledged()) {
            return;
        }

        alarmHistory.acknowledge();
        alarmHistoryMapper.updateAcknowledged(id, alarmHistory.getAcknowledgedAt());

        // WebSocket으로 알람 승인 상태 전송
        String destination = "/topic/alarms/ack/" + alarmHistory.getEquipmentCode();
        messagingTemplate.convertAndSend(destination, alarmHistory);
    }

    public List<AlarmHistory> getAlarmHistory(
            String equipmentCode,
            LocalDateTime startTime,
            LocalDateTime endTime) {
        return alarmHistoryMapper.findByEquipmentCodeAndPeriod(equipmentCode, startTime, endTime);
    }

    public List<AlarmStatisticsDto> getAlarmStatistics(
            String equipmentCode,
            LocalDateTime startTime,
            LocalDateTime endTime) {
        return alarmHistoryMapper.getAlarmStatistics(equipmentCode, startTime, endTime);
    }

    public List<AlarmHistory> searchAlarms(
            String equipmentCode,
            String sensorType,
            AlarmHistory.AlarmType alarmType,
            Boolean acknowledged,
            LocalDateTime startTime,
            LocalDateTime endTime) {
        return alarmHistoryMapper.findByConditions(
                equipmentCode,
                sensorType,
                alarmType,
                acknowledged,
                startTime,
                endTime);
    }
}