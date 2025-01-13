package org.hae.server.domain.alarm.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.hae.server.domain.alarm.model.AlarmSetting;

import java.util.List;
import java.util.Optional;

@Mapper
public interface AlarmSettingMapper {
    void insert(AlarmSetting alarmSetting);

    Optional<AlarmSetting> findById(Long id);

    Optional<AlarmSetting> findByEquipmentCodeAndSensorType(
            @Param("equipmentCode") String equipmentCode,
            @Param("sensorType") String sensorType);

    List<AlarmSetting> findByEquipmentCode(String equipmentCode);

    void update(AlarmSetting alarmSetting);

    void delete(Long id);
}