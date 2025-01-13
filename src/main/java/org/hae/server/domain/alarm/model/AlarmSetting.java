package org.hae.server.domain.alarm.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlarmSetting {
    private Long id;
    private String equipmentCode;
    private String sensorType;
    private Double minThreshold;
    private Double maxThreshold;
    private String description;
    private boolean enabled;

    public void update(Double minThreshold, Double maxThreshold, String description, Boolean enabled) {
        if (minThreshold != null)
            this.minThreshold = minThreshold;
        if (maxThreshold != null)
            this.maxThreshold = maxThreshold;
        if (description != null)
            this.description = description;
        if (enabled != null)
            this.enabled = enabled;
    }
}