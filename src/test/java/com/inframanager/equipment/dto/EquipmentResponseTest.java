package com.inframanager.equipment.dto;


import com.inframanager.equipment.domain.Equipment;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


class EquipmentResponseTest {
    @Test
    void shouldConvertEquipmentToResponse() {
        Equipment equipment = new Equipment(
                "Routeur principal",
                "Cisco",
                "Catalyst 9200"
        );

        equipment.setIpAddress("192.168.1.1");
        EquipmentResponse response = EquipmentResponse.from(equipment);


        assertEquals(equipment.getId(), response.id());
        assertEquals(equipment.getName(), response.name());
        assertEquals(equipment.getManufacturer(), response.manufacturer());
        assertEquals(equipment.getModel(), response.model());
        assertEquals(equipment.getIpAddress(), response.ipAddress());
    }
}