package org.hae.server.domain.sensor.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SensorStatisticsResponse {
    private String equipmentCode;
    private String sensorType;
    private Double minValue;
    private Double maxValue;
    private Double avgValue;
    private Long count;
    private String period; // HOUR, DAY, MONTH
}