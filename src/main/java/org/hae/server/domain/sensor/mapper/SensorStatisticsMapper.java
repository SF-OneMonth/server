package org.hae.server.domain.sensor.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.hae.server.domain.sensor.dto.response.SensorStatisticsResponse;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface SensorStatisticsMapper {

    List<SensorStatisticsResponse> findHourlyStatistics(
            @Param("equipmentCode") String equipmentCode,
            @Param("sensorType") String sensorType,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    List<SensorStatisticsResponse> findDailyStatistics(
            @Param("equipmentCode") String equipmentCode,
            @Param("sensorType") String sensorType,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    List<SensorStatisticsResponse> findMonthlyStatistics(
            @Param("equipmentCode") String equipmentCode,
            @Param("sensorType") String sensorType,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    List<SensorStatisticsResponse> findYearlyStatistics(
            @Param("equipmentCode") String equipmentCode,
            @Param("sensorType") String sensorType,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);
}