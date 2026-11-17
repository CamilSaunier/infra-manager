package com.inframanager.equipment.service;

import com.inframanager.equipment.domain.Equipment;
import com.inframanager.equipment.persistence.entity.EquipmentEntity;
import com.inframanager.equipment.repository.EquipmentRepository;
import org.springframework.stereotype.Service;

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
                .map(entity -> new Equipment(
                        entity.getName(),
                        entity.getManufacturer(),
                        entity.getModel()
                ))
                .toList();
    }

    public Equipment createEquipment(String name, String manufacturer, String model) {
        Equipment newEquipment = new Equipment(name, manufacturer, model);

        EquipmentEntity entity = new EquipmentEntity();
        entity.setName(newEquipment.getName());
        entity.setManufacturer(newEquipment.getManufacturer());
        entity.setModel(newEquipment.getModel());

        EquipmentEntity savedEntity = equipmentRepository.save(entity);

        return new Equipment(
                savedEntity.getName(),
                savedEntity.getManufacturer(),
                savedEntity.getModel()
        );
    }
}