package org.hae.server.domain.alarm.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.hae.server.domain.alarm.model.AlarmHistory;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
public class AlarmHistorySearchRequest {
    private String equipmentCode;
    private String sensorType;
    private AlarmHistory.AlarmType alarmType;
    private Boolean acknowledged;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startTime;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime endTime;
}