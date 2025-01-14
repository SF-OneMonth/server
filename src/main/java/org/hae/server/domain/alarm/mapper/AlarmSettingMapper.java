package org.hae.server.domain.alarm.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.hae.server.domain.alarm.model.AlarmSetting;

import java.util.Optional;

@Mapper
public interface AlarmSettingMapper {
    Optional<AlarmSetting> findByEquipmentCodeAndSensorType(
            @Param("equipmentCode") String equipmentCode,
            @Param("sensorType") String sensorType);
}