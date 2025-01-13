package org.hae.server.domain.equipment.model;

public enum EquipmentStatus {
    RUNNING("가동중"),
    STOPPED("정지"),
    ERROR("에러");

    private final String description;

    EquipmentStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}