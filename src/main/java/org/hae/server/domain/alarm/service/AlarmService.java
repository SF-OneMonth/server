package org.hae.server.domain.alarm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hae.server.domain.alarm.dto.response.AlarmStatisticsDto;
import org.hae.server.domain.alarm.mapper.AlarmHistoryMapper;
import org.hae.server.domain.alarm.mapper.AlarmSettingMapper;
import org.hae.server.domain.alarm.model.AlarmHistory;
import org.hae.server.domain.alarm.model.AlarmSetting;
import org.hae.server.domain.sensor.model.SensorData;
import org.hae.server.domain.sensor.service.SensorWebSocketService;
import org.hae.server.global.common.exception.SFaaSException;
import org.hae.server.global.common.response.ErrorType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlarmService {
    private final AlarmSettingMapper alarmSettingMapper;
    private final AlarmHistoryMapper alarmHistoryMapper;
    private final SensorWebSocketService webSocketService;

    public void processAlarm(SensorData sensorData) {
        // 알람 설정 조회
        Optional<AlarmSetting> alarmSettingOptional = alarmSettingMapper.findByEquipmentCodeAndSensorType(
                sensorData.getEquipmentCode(),
                sensorData.getSensorType());

        if (alarmSettingOptional.isEmpty()) {
            log.debug("알람 설정이 없음: equipmentCode={}, sensorType={}",
                    sensorData.getEquipmentCode(), sensorData.getSensorType());
            return;
        }

        // 알람 조건 체크 및 생성
        AlarmHistory alarmHistory = createAlarmIfNeeded(sensorData, alarmSettingOptional.get());
        if (alarmHistory != null) {
            // 알람 저장
            alarmHistoryMapper.insert(alarmHistory);
            // WebSocket으로 알람 전송
            webSocketService.sendSensorAlarm(sensorData.getEquipmentCode(), alarmHistory);
            log.info("알람 생성 및 전송 완료: {}", alarmHistory);
        }
    }

    private AlarmHistory createAlarmIfNeeded(SensorData sensorData, AlarmSetting alarmSetting) {
        if (!alarmSetting.isEnabled()) {
            return null;
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

        if (alarmType == null) {
            return null;
        }

        return AlarmHistory.builder()
                .equipmentCode(sensorData.getEquipmentCode())
                .sensorType(sensorData.getSensorType())
                .sensorValue(sensorData.getValue())
                .thresholdValue(thresholdValue)
                .alarmType(alarmType)
                .occurredAt(sensorData.getTimestamp())
                .acknowledged(false)
                .build();
    }

    @Transactional(readOnly = true)
    public List<AlarmHistory> getAlarmHistory(String equipmentCode, LocalDateTime start, LocalDateTime end) {
        return alarmHistoryMapper.findByEquipmentCodeAndPeriod(equipmentCode, start, end);
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
        webSocketService.sendSensorAlarm(alarmHistory.getEquipmentCode(), alarmHistory);
        log.info("알람 확인 처리 완료: {}", id);
    }

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
    public List<AlarmStatisticsDto> getAlarmStatistics(String equipmentCode, LocalDateTime start, LocalDateTime end) {
        return alarmHistoryMapper.getAlarmStatistics(equipmentCode, start, end);
    }
}