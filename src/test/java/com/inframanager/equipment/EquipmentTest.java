package com.inframanager.equipment;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EquipmentTest {

    @Test
    public void shouldCreateEquipmentWithValidData() {
        Equipment equipment = new Equipment(
                "Routeur principal",
                "Cisco",
                "Catalyst 9200"
        );

        assertEquals("Routeur principal", equipment.getName());
        assertEquals("Cisco", equipment.getManufacturer());
        assertEquals("Catalyst 9200", equipment.getModel());
    }
}
