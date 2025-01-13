package org.hae.server.domain.equipment.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Equipment {
    private Long id;
    private String equipmentCode;
    private String equipmentName;
    private String equipmentType;
    private EquipmentStatus status;
    private LocalDate installationDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}