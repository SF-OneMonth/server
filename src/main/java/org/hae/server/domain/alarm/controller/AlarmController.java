package org.hae.server.domain.alarm.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hae.server.domain.alarm.dto.request.AlarmHistorySearchRequest;
import org.hae.server.domain.alarm.model.AlarmHistory;
import org.hae.server.domain.alarm.dto.response.AlarmStatisticsDto;
import org.hae.server.domain.alarm.service.AlarmService;
import org.hae.server.global.common.response.SFaaSResponse;
import org.hae.server.global.common.response.SuccessType;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/alarms")
@RequiredArgsConstructor
public class AlarmController {

    private final AlarmService alarmService;

    @GetMapping("/history/{equipmentCode}")
    public SFaaSResponse<List<AlarmHistory>> getAlarmHistory(
            @PathVariable String equipmentCode,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return SFaaSResponse.success(
                SuccessType.OK,
                alarmService.getAlarmHistory(equipmentCode, start, end));
    }

    @PutMapping("/history/{id}/acknowledge")
    public SFaaSResponse<?> acknowledgeAlarm(@PathVariable Long id) {
        alarmService.acknowledgeAlarm(id);
        return SFaaSResponse.success(SuccessType.OK);
    }

    @GetMapping("/statistics/{equipmentCode}")
    public SFaaSResponse<List<AlarmStatisticsDto>> getAlarmStatistics(
            @PathVariable String equipmentCode,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return SFaaSResponse.success(
                SuccessType.OK,
                alarmService.getAlarmStatistics(equipmentCode, start, end));
    }

    @GetMapping("/search")
    public SFaaSResponse<List<AlarmHistory>> searchAlarms(
            @Valid @ModelAttribute AlarmHistorySearchRequest request) {
        return SFaaSResponse.success(
                SuccessType.OK,
                alarmService.searchAlarms(
                        request.getEquipmentCode(),
                        request.getSensorType(),
                        request.getAlarmType(),
                        request.getAcknowledged(),
                        request.getStartTime(),
                        request.getEndTime()));
    }
}