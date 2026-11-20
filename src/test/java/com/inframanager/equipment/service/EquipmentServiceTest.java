package com.inframanager.equipment.service;

import com.inframanager.equipment.domain.Equipment;
import com.inframanager.equipment.persistence.entity.EquipmentEntity;
import com.inframanager.equipment.repository.EquipmentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
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

    @Test
    public void ShouldThrowNotFoundWhenEquipmentDoesNotExist() {
        EquipmentRepository repository = mock(EquipmentRepository.class);
        when(repository.findById(222L)).thenReturn(Optional.empty());
        EquipmentService service = new EquipmentService((repository));
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> service.getEquipmentById(222L)
        );
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(repository).findById(222L);
    }

    @Test
    public void shouldGetAListOfEquipment() {
        EquipmentRepository repository = mock(EquipmentRepository.class);

        EquipmentEntity firstEntity = new EquipmentEntity();
        firstEntity.setName("Routeur principal");
        firstEntity.setManufacturer("Cisco");
        firstEntity.setModel("Catalyst 9200");

        EquipmentEntity secondEntity = new EquipmentEntity();
        secondEntity.setName("Switch secondaire");
        secondEntity.setManufacturer("HP");
        secondEntity.setModel("Aruba 6100");

        when(repository.findAll()).thenReturn(List.of(firstEntity, secondEntity));

        EquipmentService service = new EquipmentService(repository);

        List<Equipment> equipmentList = service.getAllEquipment();

        assertEquals(2, equipmentList.size());
        assertEquals("Routeur principal", equipmentList.getFirst().getName());
        verify(repository, times(1)).findAll();
    }

    @Test
    public void shouldUpdateEquipment() {
        EquipmentRepository repository = mock(EquipmentRepository.class);
        EquipmentEntity entity = new EquipmentEntity();
        entity.setName("Routeur principal");
        entity.setManufacturer("Cisco");
        entity.setModel("Catalyst 9200");

        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(repository.save(any(EquipmentEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        EquipmentService service = new EquipmentService(repository);

        Equipment updatedEquipment = service.updateEquipment(
                1L,
                "Switch principal",
                "HP",
                "Aruba 6100"
        );

        assertEquals("Switch principal", updatedEquipment.getName());
        assertEquals("HP", updatedEquipment.getManufacturer());
        assertEquals("Aruba 6100", updatedEquipment.getModel());

        verify(repository, times(1)).save(any(EquipmentEntity.class));
    }

    @Test
    public void shouldThrowNotFoundWhenUpdatingNonExistingEquipment() {
        EquipmentRepository repository = mock(EquipmentRepository.class);
        when(repository.findById(222L)).thenReturn(Optional.empty());
        EquipmentService service = new EquipmentService((repository));
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> service.updateEquipment(222L,
                        "Switch principal",
                        "HP",
                        "Aruba 6100")
        );
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(repository).findById(222L);
    }

    @Test
    public void shouldThrowConflictWhenUpdatingEquipmentWithExistingName() {
        EquipmentRepository repository = mock(EquipmentRepository.class);

        EquipmentEntity entity = new EquipmentEntity();
        entity.setName("Routeur principal");
        entity.setManufacturer("Cisco");
        entity.setModel("Catalyst 9200");

        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(repository.save(any(EquipmentEntity.class)))
                .thenThrow(new DataIntegrityViolationException("Duplicate name"));

        EquipmentService service = new EquipmentService(repository);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> service.updateEquipment(
                        1L,
                        "Switch principal",
                        "HP",
                        "Aruba 6100"
                )
        );

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
    }

}
