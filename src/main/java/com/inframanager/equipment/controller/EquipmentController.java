
package com.inframanager.equipment.controller;

import com.inframanager.equipment.domain.Equipment;
import com.inframanager.equipment.dto.EquipmentResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/equipment")
public class EquipmentController {

    @GetMapping
    public EquipmentResponse getEquipment() {
        Equipment equipment = new Equipment(
                "Routeur principal",
                "Cisco",
                "Catalyst 9200"
        );

        return EquipmentResponse.from(equipment);
    }
}
