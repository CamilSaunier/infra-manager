package com.inframanager.equipment.dto;

import com.inframanager.equipment.domain.Equipment;

public record EquipmentResponse(
        Long id,
        String name,
        String manufacturer,
        String model,
        String ipAddress
) {

    public static EquipmentResponse from(Equipment equipment) {

        return new EquipmentResponse(
                equipment.getId(),
                equipment.getName(),
                equipment.getManufacturer(),
                equipment.getModel(),
                equipment.getIpAddress());
    }


}