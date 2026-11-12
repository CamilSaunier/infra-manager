package com.inframanager.equipment.service;

import com.inframanager.equipment.domain.Equipment;
import com.inframanager.equipment.repository.EquipmentRepository;

public class EquipmentService {

    public EquipmentService(EquipmentRepository equipmentRepository) {
        this.equipmentRepository = equipmentRepository;
    }

    private final EquipmentRepository equipmentRepository;

    public Equipment createEquipment(
            String name,
            String manufacturer,
            String model
    ) {
        Equipment newEquipment = new Equipment(name, manufacturer, model);
        return equipmentRepository.save(newEquipment);
    }
}