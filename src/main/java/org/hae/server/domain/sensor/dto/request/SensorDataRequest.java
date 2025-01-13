package org.hae.server.domain.sensor.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.hae.server.domain.sensor.model.SensorData;

import java.time.LocalDateTime;

@Getter
public class SensorDataRequest {
    @NotBlank(message = "설비 코드는 필수입니다")
    private String equipmentCode;

    @NotBlank(message = "센서 타입은 필수입니다")
    private String sensorType;

    @NotNull(message = "센서 값은 필수입니다")
    private Double value;

    public SensorData toEntity() {
        return SensorData.builder()
                .equipmentCode(equipmentCode)
                .sensorType(sensorType)
                .value(value)
                .timestamp(LocalDateTime.now())
                .build();
    }
}