package org.hae.server.domain.alarm.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.hae.server.domain.alarm.model.AlarmHistory;
import org.hae.server.domain.alarm.dto.response.AlarmStatisticsDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Mapper
public interface AlarmHistoryMapper {
    void insert(AlarmHistory alarmHistory);

    Optional<AlarmHistory> findById(Long id);

    void updateAcknowledged(@Param("id") Long id,
            @Param("acknowledgedAt") LocalDateTime acknowledgedAt);

    List<AlarmHistory> findByEquipmentCodeAndPeriod(
            @Param("equipmentCode") String equipmentCode,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);

    List<AlarmStatisticsDto> getAlarmStatistics(
            @Param("equipmentCode") String equipmentCode,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);

    List<AlarmHistory> findByConditions(
            @Param("equipmentCode") String equipmentCode,
            @Param("sensorType") String sensorType,
            @Param("alarmType") AlarmHistory.AlarmType alarmType,
            @Param("acknowledged") Boolean acknowledged,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);
}