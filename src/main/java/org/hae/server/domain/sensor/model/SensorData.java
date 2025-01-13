package org.hae.server.domain.sensor.model;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Getter
@Builder
@Document(collection = "sensor_data")
public class SensorData {
    @Field("equipment_code")
    private String equipmentCode;

    @Field("sensor_type")
    private String sensorType;

    @Field("value")
    private Double value;

    @Field("timestamp")
    private LocalDateTime timestamp;
}