package com.inframanager.equipment.service;

import com.inframanager.equipment.domain.Equipment;
import com.inframanager.equipment.repository.EquipmentRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class EquipmentServiceTest {

    private static class FakeEquipmentRepository implements EquipmentRepository {

        @Override
        public Equipment save(Equipment equipment) {
            return equipment;
        }
    }

    @Test
    public void shouldCreateEquipment() {
        EquipmentRepository repository = new FakeEquipmentRepository();
        EquipmentService service = new EquipmentService(repository);

        Equipment equipment = service.createEquipment(
                "Routeur principal",
                "Cisco",
                "Catalyst 9200"
        );

        assertEquals("Routeur principal", equipment.getName());
    }

}
