package org.hae.server.domain.alarm.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlarmHistory {
    private Long id;
    private String equipmentCode;
    private String sensorType;
    private Double sensorValue;
    private Double thresholdValue;
    private AlarmType alarmType;
    private LocalDateTime occurredAt;
    private boolean acknowledged;
    private LocalDateTime acknowledgedAt;

    public void acknowledge() {
        this.acknowledged = true;
        this.acknowledgedAt = LocalDateTime.now();
    }

    public enum AlarmType {
        HIGH("상한값 초과"),
        LOW("하한값 미달");

        private final String description;

        AlarmType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}