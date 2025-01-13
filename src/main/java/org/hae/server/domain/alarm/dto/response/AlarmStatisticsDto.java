package org.hae.server.domain.alarm.dto.response;

import lombok.Getter;
import lombok.Setter;
import org.hae.server.domain.alarm.model.AlarmHistory;

@Getter
@Setter
public class AlarmStatisticsDto {
    private String equipmentCode;
    private String sensorType;
    private AlarmHistory.AlarmType alarmType;
    private Long count;
    private Long acknowledgedCount;
}