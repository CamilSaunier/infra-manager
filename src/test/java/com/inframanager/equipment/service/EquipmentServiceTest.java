package com.inframanager.equipment.service;

import com.inframanager.equipment.domain.Equipment;
import com.inframanager.equipment.persistence.entity.EquipmentEntity;
import com.inframanager.equipment.repository.EquipmentRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class EquipmentServiceTest {

    @Test
    public void shouldCreateEquipment() {
        EquipmentRepository repository = mock(EquipmentRepository.class);

        when(repository.save(any(EquipmentEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        EquipmentService service = new EquipmentService(repository);

        Equipment equipment = service.createEquipment(
                "Routeur principal",
                "Cisco",
                "Catalyst 9200"
        );

        assertEquals("Routeur principal", equipment.getName());

        verify(repository).save(any(EquipmentEntity.class));
    }
}
