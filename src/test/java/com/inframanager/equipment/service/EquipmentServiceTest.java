package com.inframanager.equipment.service;

import com.inframanager.equipment.domain.Equipment;
import com.inframanager.equipment.entity.EquipmentEntity;
import com.inframanager.equipment.repository.EquipmentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public void shouldSearchEquipmentByNameOrManufacturer() {
        EquipmentRepository repository = mock(EquipmentRepository.class);

        EquipmentEntity entity = new EquipmentEntity();
        entity.setName("Switch principal");
        entity.setManufacturer("Cisco");
        entity.setModel("Catalyst 9200");

        Pageable pageable = PageRequest.of(0, 10);

        Page<EquipmentEntity> page = new PageImpl<>(
                List.of(entity),
                pageable,
                1
        );

        when(repository
                .findByNameContainingIgnoreCaseOrManufacturerContainingIgnoreCase(
                        "cisco",
                        "cisco",
                        pageable
                )).thenReturn(page);

        EquipmentService service = new EquipmentService(repository);

        Page<Equipment> result = service.getAllEquipment("cisco", pageable);

        assertEquals(1, result.getContent().size());
        assertEquals("Switch principal", result.getContent().getFirst().getName());

        verify(repository, times(1))
                .findByNameContainingIgnoreCaseOrManufacturerContainingIgnoreCase(
                        "cisco",
                        "cisco",
                        pageable
                );
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

    @Test
    public void shouldDeleteEquipment() {
        EquipmentRepository repository = mock(EquipmentRepository.class);

        EquipmentEntity entity = new EquipmentEntity();
        entity.setName("Routeur principal");
        entity.setManufacturer("Cisco");
        entity.setModel("Catalyst 9200");

        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        EquipmentService service = new EquipmentService(repository);

        service.deleteEquipment(1L);

        verify(repository, times(1)).delete(entity);
    }

    @Test
    public void shouldThrowNotFoundWhenDeletingNonExistingEquipment() {
        EquipmentRepository repository = mock(EquipmentRepository.class);

        when(repository.findById(222L)).thenReturn(Optional.empty());

        EquipmentService service = new EquipmentService(repository);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> service.deleteEquipment(222L)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());

        verify(repository, never()).delete(any(EquipmentEntity.class));
    }
}
