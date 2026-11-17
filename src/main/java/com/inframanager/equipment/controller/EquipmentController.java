
package com.inframanager.equipment.controller;

import com.inframanager.equipment.dto.EquipmentRequest;
import com.inframanager.equipment.dto.EquipmentResponse;
import com.inframanager.equipment.service.EquipmentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipment")
public class EquipmentController {

    private final EquipmentService equipmentService;

    public EquipmentController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    @GetMapping
    public List<EquipmentResponse> getEquipment() {
        return equipmentService.getAllEquipment()
                .stream()
                .map(EquipmentResponse::from)
                .toList();
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
