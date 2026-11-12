package com.inframanager.equipment.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/equipment")
public class EquipmentController {

    @GetMapping
    public String getEquipment() {
        return "Liste des équipements";
    }
}
