package org.hae.server.domain.sensor.controller;

import lombok.RequiredArgsConstructor;
import org.hae.server.domain.sensor.dto.response.SensorStatisticsResponse;
import org.hae.server.domain.sensor.service.SensorStatisticsService;
import org.hae.server.global.common.response.SFaaSResponse;
import org.hae.server.global.common.response.SuccessType;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/sensor-statistics")
@RequiredArgsConstructor
public class SensorStatisticsController {

    private final SensorStatisticsService sensorStatisticsService;

    @GetMapping("/hourly/{equipmentCode}")
    public SFaaSResponse<List<SensorStatisticsResponse>> getHourlyStatistics(
            @PathVariable String equipmentCode,
            @RequestParam String sensorType,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {

        return SFaaSResponse.success(
                SuccessType.OK,
                sensorStatisticsService.getHourlyStatistics(equipmentCode, sensorType, start, end));
    }

    @GetMapping("/daily/{equipmentCode}")
    public SFaaSResponse<List<SensorStatisticsResponse>> getDailyStatistics(
            @PathVariable String equipmentCode,
            @RequestParam String sensorType,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {

        return SFaaSResponse.success(
                SuccessType.OK,
                sensorStatisticsService.getDailyStatistics(equipmentCode, sensorType, start, end));
    }

    @GetMapping("/monthly/{equipmentCode}")
    public SFaaSResponse<List<SensorStatisticsResponse>> getMonthlyStatistics(
            @PathVariable String equipmentCode,
            @RequestParam String sensorType,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {

        return SFaaSResponse.success(
                SuccessType.OK,
                sensorStatisticsService.getMonthlyStatistics(equipmentCode, sensorType, start, end));
    }

    @GetMapping("/yearly/{equipmentCode}")
    public SFaaSResponse<List<SensorStatisticsResponse>> getYearlyStatistics(
            @PathVariable String equipmentCode,
            @RequestParam String sensorType,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {

        return SFaaSResponse.success(
                SuccessType.OK,
                sensorStatisticsService.getYearlyStatistics(equipmentCode, sensorType, start, end));
    }
}