package org.hae.server.domain.equipment.service;

import lombok.RequiredArgsConstructor;
import org.hae.server.domain.equipment.mapper.EquipmentMapper;
import org.hae.server.domain.equipment.model.Equipment;
import org.hae.server.global.common.exception.SFaaSException;
import org.hae.server.global.common.response.ErrorType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EquipmentService {

    private final EquipmentMapper equipmentMapper;

    public List<Equipment> getAllEquipments() {
        return equipmentMapper.findAll();
    }

    public Equipment getEquipment(Long id) {
        return equipmentMapper.findById(id)
                .orElseThrow(() -> new SFaaSException(ErrorType.EQUIPMENT_NOT_FOUND));
    }

    public Equipment getEquipmentByCode(String code) {
        return equipmentMapper.findByCode(code)
                .orElseThrow(() -> new SFaaSException(ErrorType.EQUIPMENT_NOT_FOUND));
    }

    @Transactional
    public void createEquipment(Equipment equipment) {
        equipmentMapper.findByCode(equipment.getEquipmentCode())
                .ifPresent(e -> {
                    throw new SFaaSException(ErrorType.DUPLICATE_EQUIPMENT_CODE);
                });

        equipmentMapper.insert(equipment);
    }

    @Transactional
    public void updateEquipment(Equipment equipment) {
        equipmentMapper.findById(equipment.getId())
                .orElseThrow(() -> new SFaaSException(ErrorType.NOT_FOUND));

        equipmentMapper.update(equipment);
    }

    @Transactional
    public void deleteEquipment(Long id) {
        equipmentMapper.findById(id)
                .orElseThrow(() -> new SFaaSException(ErrorType.NOT_FOUND));

        equipmentMapper.delete(id);
    }
}