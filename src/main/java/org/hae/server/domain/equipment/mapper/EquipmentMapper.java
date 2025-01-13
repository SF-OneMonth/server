package org.hae.server.domain.equipment.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.hae.server.domain.equipment.model.Equipment;

import java.util.List;
import java.util.Optional;

@Mapper
public interface EquipmentMapper {
    List<Equipment> findAll();

    Optional<Equipment> findById(Long id);

    Optional<Equipment> findByCode(String equipmentCode);

    void insert(Equipment equipment);

    void update(Equipment equipment);

    void delete(Long id);
}