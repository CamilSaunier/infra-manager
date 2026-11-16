
package com.inframanager.equipment.controller;

import com.inframanager.equipment.domain.Equipment;
import com.inframanager.equipment.dto.EquipmentRequest;
import com.inframanager.equipment.dto.EquipmentResponse;
import com.inframanager.equipment.service.EquipmentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/equipment")
public class EquipmentController {

    private final EquipmentService equipmentService;

    public EquipmentController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    @GetMapping
    public EquipmentResponse getEquipment() {
        Equipment equipment = new Equipment(
                "Routeur principal",
                "Cisco",
                "Catalyst 9200"
        );

        return EquipmentResponse.from(equipment);
    }

    @PostMapping
    public void createEquipment(@RequestBody EquipmentRequest equipmentBody) {
        equipmentService.createEquipment(
                equipmentBody.name(),
                equipmentBody.manufacturer(),
                equipmentBody.model()
        );
    }
}
