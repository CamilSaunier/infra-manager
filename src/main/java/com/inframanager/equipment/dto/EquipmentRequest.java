package com.inframanager.equipment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Data required to create or update network equipment")
public record EquipmentRequest(

        @Schema(description = "Name of the equipment", example = "Core Switch")
        @NotBlank
        @Size(max = 100)
        String name,

        @Schema(description = "Equipment manufacturer", example = "Cisco")
        @NotBlank
        @Size(max = 100)
        String manufacturer,

        @Schema(description = "Equipment model", example = "Catalyst 9300")
        @NotBlank
        @Size(max = 100)
        String model,

        @Schema(
                description = "IPv4 address of the equipment",
                example = "192.168.1.10"
        )
        @Pattern(regexp = "^([0-9]{1,3}\\.){3}[0-9]{1,3}$")
        String ipAddress

) {
}