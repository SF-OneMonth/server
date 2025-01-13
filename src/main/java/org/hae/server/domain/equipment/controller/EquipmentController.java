package org.hae.server.domain.equipment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hae.server.domain.equipment.dto.request.EquipmentCreateRequest;
import org.hae.server.domain.equipment.dto.request.EquipmentUpdateRequest;
import org.hae.server.domain.equipment.dto.response.EquipmentResponse;
import org.hae.server.domain.equipment.service.EquipmentService;
import org.hae.server.global.common.response.SFaaSResponse;
import org.hae.server.global.common.response.SuccessType;
import org.springframework.web.bind.annotation.*;
import org.hae.server.domain.equipment.model.Equipment;

import java.util.List;

@RestController
@RequestMapping("/api/equipments")
@RequiredArgsConstructor
public class EquipmentController {

    private final EquipmentService equipmentService;

    @GetMapping
    public SFaaSResponse<List<EquipmentResponse>> getAllEquipments() {
        return SFaaSResponse.success(
                SuccessType.OK,
                equipmentService.getAllEquipments().stream()
                        .map(EquipmentResponse::from)
                        .toList());
    }

    @GetMapping("/{id}")
    public SFaaSResponse<EquipmentResponse> getEquipment(@PathVariable Long id) {
        return SFaaSResponse.success(
                SuccessType.OK,
                EquipmentResponse.from(equipmentService.getEquipment(id)));
    }

    @PostMapping
    public SFaaSResponse<?> createEquipment(@Valid @RequestBody EquipmentCreateRequest request) {
        equipmentService.createEquipment(request.toEntity());
        return SFaaSResponse.success(SuccessType.CREATED);
    }

    @PutMapping("/{id}")
    public SFaaSResponse<?> updateEquipment(
            @PathVariable Long id,
            @Valid @RequestBody EquipmentUpdateRequest request) {
        Equipment equipment = equipmentService.getEquipment(id);
        equipmentService.updateEquipment(request.toEntity(id, equipment.getEquipmentCode()));
        return SFaaSResponse.success(SuccessType.OK);
    }

    @DeleteMapping("/{id}")
    public SFaaSResponse<?> deleteEquipment(@PathVariable Long id) {
        equipmentService.deleteEquipment(id);
        return SFaaSResponse.success(SuccessType.OK);
    }
}