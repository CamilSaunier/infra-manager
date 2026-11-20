package com.inframanager.equipment.service;

import com.inframanager.equipment.domain.Equipment;
import com.inframanager.equipment.persistence.entity.EquipmentEntity;
import com.inframanager.equipment.repository.EquipmentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        assertEquals("Cisco", equipment.getManufacturer());
        assertEquals("Catalyst 9200", equipment.getModel());

        verify(repository, times(1)).save(any(EquipmentEntity.class));
    }

    @Test
    public void shouldThrowConflictWhenEquipmentNameAlreadyExists() {
        EquipmentRepository repository = mock(EquipmentRepository.class);
        when(repository.existsByName("Routeur principal"))
                .thenReturn(true);
        EquipmentService service = new EquipmentService(repository);
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> service.createEquipment(
                        "Routeur principal",
                        "Cisco",
                        "Catalyst 9200"
                )
        );
        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        verify(repository, never()).save(any(EquipmentEntity.class));
    }

    @Test
    public void ShouldGetAnEquipmentById() {
        EquipmentRepository repository = mock(EquipmentRepository.class);
        EquipmentEntity entity = new EquipmentEntity();
        entity.setName("Routeur principal");
        entity.setManufacturer("Cisco");
        entity.setModel("Catalyst 9200");
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        EquipmentService service = new EquipmentService((repository));
        Equipment equipment = service.getEquipmentById(1L);
        verify(repository).findById(1L);

        assertEquals("Routeur principal", equipment.getName());
        assertEquals("Cisco", equipment.getManufacturer());
        assertEquals("Catalyst 9200", equipment.getModel());
    }


}
