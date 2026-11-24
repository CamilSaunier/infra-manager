package com.inframanager.equipment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record EquipmentRequest(
        @NotBlank
        @Size(max = 100)
        String name,

        @NotBlank
        @Size(max = 100)
        String manufacturer,

        @NotBlank
        @Size(max = 100)
        String model,

        @Pattern(regexp = "^([0-9]{1,3}\\.){3}[0-9]{1,3}$")
        String ipAddress
) {

}
