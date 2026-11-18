
package com.inframanager.equipment.controller;

import com.inframanager.equipment.domain.Equipment;
import com.inframanager.equipment.dto.EquipmentRequest;
import com.inframanager.equipment.dto.EquipmentResponse;
import com.inframanager.equipment.service.EquipmentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/equipment")
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

    @GetMapping("/{id}")
    public EquipmentResponse getEquipmentById(@PathVariable Long id) {
        return EquipmentResponse.from(
                equipmentService.getEquipmentById(id)
        );
    }

    @PostMapping
    public EquipmentResponse createEquipment(@RequestBody EquipmentRequest equipmentBody) {
        Equipment newEquipment = equipmentService.createEquipment(
                equipmentBody.name(),
                equipmentBody.manufacturer(),
                equipmentBody.model()
        );
        return EquipmentResponse.from(newEquipment);
    }

    @PutMapping("/{id}")
    public EquipmentResponse updateEquipment(@PathVariable Long id, @RequestBody EquipmentRequest equipmentBody) {
        Equipment equipmentUpdated = equipmentService.updateEquipment(
                id,
                equipmentBody.name(),
                equipmentBody.manufacturer(),
                equipmentBody.model());
        return EquipmentResponse.from(equipmentUpdated);
    }
}
