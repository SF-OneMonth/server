package org.hae.server.domain.equipment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.hae.server.domain.equipment.model.Equipment;
import org.hae.server.domain.equipment.model.EquipmentStatus;

import java.time.LocalDate;

@Getter
public class EquipmentUpdateRequest {
    @NotBlank(message = "설비명은 필수입니다")
    private String equipmentName;

    @NotBlank(message = "설비 유형은 필수입니다")
    private String equipmentType;

    @NotNull(message = "설비 상태는 필수입니다")
    private EquipmentStatus status;

    @NotNull(message = "설치일자는 필수입니다")
    private LocalDate installationDate;

    public Equipment toEntity(Long id, String equipmentCode) {
        return Equipment.builder()
                .id(id)
                .equipmentCode(equipmentCode)
                .equipmentName(equipmentName)
                .equipmentType(equipmentType)
                .status(status)
                .installationDate(installationDate)
                .build();
    }
}