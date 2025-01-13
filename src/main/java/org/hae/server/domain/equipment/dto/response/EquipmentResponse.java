package org.hae.server.domain.equipment.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.hae.server.domain.equipment.model.Equipment;
import org.hae.server.domain.equipment.model.EquipmentStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class EquipmentResponse {
    private Long id;
    private String equipmentCode;
    private String equipmentName;
    private String equipmentType;
    private EquipmentStatus status;
    private LocalDate installationDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static EquipmentResponse from(Equipment equipment) {
        return EquipmentResponse.builder()
                .id(equipment.getId())
                .equipmentCode(equipment.getEquipmentCode())
                .equipmentName(equipment.getEquipmentName())
                .equipmentType(equipment.getEquipmentType())
                .status(equipment.getStatus())
                .installationDate(equipment.getInstallationDate())
                .createdAt(equipment.getCreatedAt())
                .updatedAt(equipment.getUpdatedAt())
                .build();
    }
}