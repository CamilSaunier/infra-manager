package com.inframanager.equipment.dto;

import com.inframanager.equipment.domain.Equipment;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Equipment data returned by the API")
public record EquipmentResponse(

        @Schema(description = "Unique identifier of the equipment", example = "1")
        Long id,

        @Schema(description = "Name of the equipment", example = "Core Switch")
        String name,

        @Schema(description = "Equipment manufacturer", example = "Cisco")
        String manufacturer,

        @Schema(description = "Equipment model", example = "Catalyst 9300")
        String model,

        @Schema(description = "IPv4 address of the equipment", example = "192.168.1.10")
        String ipAddress

) {

    public static EquipmentResponse from(Equipment equipment) {

        return new EquipmentResponse(
                equipment.getId(),
                equipment.getName(),
                equipment.getManufacturer(),
                equipment.getModel(),
                equipment.getIpAddress()
        );
    }
}