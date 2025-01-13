package org.hae.server.domain.sensor.service;

import lombok.RequiredArgsConstructor;
import org.hae.server.domain.sensor.dto.response.SensorStatisticsResponse;
import org.hae.server.domain.sensor.mapper.SensorStatisticsMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SensorStatisticsService {

        private final SensorStatisticsMapper sensorStatisticsMapper;

        public List<SensorStatisticsResponse> getHourlyStatistics(
                        String equipmentCode, String sensorType, LocalDateTime start, LocalDateTime end) {
                return sensorStatisticsMapper.findHourlyStatistics(equipmentCode, sensorType, start, end);
        }

        public List<SensorStatisticsResponse> getDailyStatistics(
                        String equipmentCode, String sensorType, LocalDateTime start, LocalDateTime end) {
                return sensorStatisticsMapper.findDailyStatistics(equipmentCode, sensorType, start, end);
        }

        public List<SensorStatisticsResponse> getMonthlyStatistics(
                        String equipmentCode, String sensorType, LocalDateTime start, LocalDateTime end) {
                return sensorStatisticsMapper.findMonthlyStatistics(equipmentCode, sensorType, start, end);
        }

        public List<SensorStatisticsResponse> getYearlyStatistics(
                        String equipmentCode, String sensorType, LocalDateTime start, LocalDateTime end) {
                return sensorStatisticsMapper.findYearlyStatistics(equipmentCode, sensorType, start, end);
        }
}