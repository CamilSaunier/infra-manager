package com.inframanager.equipment.service;

import com.inframanager.equipment.domain.Equipment;
import com.inframanager.equipment.persistence.entity.EquipmentEntity;
import com.inframanager.equipment.repository.EquipmentRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;

    public EquipmentService(EquipmentRepository equipmentRepository) {
        this.equipmentRepository = equipmentRepository;
    }

    public List<Equipment> getAllEquipment() {
        return equipmentRepository.findAll()
                .stream()
                .map(entity -> Equipment.reconstitute(
                        entity.getId(),
                        entity.getName(),
                        entity.getManufacturer(),
                        entity.getModel(),
                        entity.getIpAddress()
                ))
                .toList();
    }

    public Equipment getEquipmentById(Long id) {
        EquipmentEntity entity = equipmentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Equipment not found"
                ));

        return Equipment.reconstitute(
                entity.getId(),
                entity.getName(),
                entity.getManufacturer(),
                entity.getModel(),
                entity.getIpAddress()
        );
    }

    public Equipment createEquipment(String name, String manufacturer, String model) {
        if (equipmentRepository.existsByName(name)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Equipment name already exists"
            );
        }

        Equipment newEquipment = new Equipment(name, manufacturer, model);

        EquipmentEntity entity = new EquipmentEntity();
        entity.setName(newEquipment.getName());
        entity.setManufacturer(newEquipment.getManufacturer());
        entity.setModel(newEquipment.getModel());
        EquipmentEntity savedEntity;
        try {
            savedEntity = equipmentRepository.save(entity);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Equipment name already exists",
                    e
            );
        }
        return Equipment.reconstitute(
                savedEntity.getId(),
                savedEntity.getName(),
                savedEntity.getManufacturer(),
                savedEntity.getModel(),
                savedEntity.getIpAddress()
        );

    }

    public Equipment updateEquipment(Long id, String name, String manufacturer, String model) {
        EquipmentEntity equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Equipment not found"
                ));
        equipment.setName(name);
        equipment.setModel(model);
        equipment.setManufacturer(manufacturer);
        EquipmentEntity savedEntity;
        try {
            savedEntity = equipmentRepository.save(equipment);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Equipment name already exists",
                    e
            );
        }
        return Equipment.reconstitute(
                savedEntity.getId(),
                savedEntity.getName(),
                savedEntity.getManufacturer(),
                savedEntity.getModel(),
                savedEntity.getIpAddress()
        );
    }
}