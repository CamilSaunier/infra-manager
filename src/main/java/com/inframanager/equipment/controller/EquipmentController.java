
package com.inframanager.equipment.controller;

import com.inframanager.equipment.domain.Equipment;
import com.inframanager.equipment.dto.EquipmentRequest;
import com.inframanager.equipment.dto.EquipmentResponse;
import com.inframanager.equipment.service.EquipmentService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/equipment")
public class EquipmentController {

    private final EquipmentService equipmentService;

    public EquipmentController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    @GetMapping
    public Page<EquipmentResponse> getEquipment(
            @RequestParam(required = false) String search,
            Pageable pageable
    ) {
        return equipmentService.getAllEquipment(search, pageable)
                .map(EquipmentResponse::from);
    }

    @GetMapping("/{id}")
    public EquipmentResponse getEquipmentById(@PathVariable Long id) {
        return EquipmentResponse.from(
                equipmentService.getEquipmentById(id)
        );
    }

    @PostMapping
    public EquipmentResponse createEquipment(@Valid @RequestBody EquipmentRequest equipmentBody) {
        Equipment newEquipment = equipmentService.createEquipment(
                equipmentBody.name(),
                equipmentBody.manufacturer(),
                equipmentBody.model()
        );
        return EquipmentResponse.from(newEquipment);
    }

    @PutMapping("/{id}")
    public EquipmentResponse updateEquipment(@PathVariable Long id, @Valid @RequestBody EquipmentRequest equipmentBody) {
        Equipment equipmentUpdated = equipmentService.updateEquipment(
                id,
                equipmentBody.name(),
                equipmentBody.manufacturer(),
                equipmentBody.model());
        return EquipmentResponse.from(equipmentUpdated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEquipment(@PathVariable Long id) {
        equipmentService.deleteEquipment(id);
        return ResponseEntity.noContent().build();
    }
}
