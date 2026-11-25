package com.inframanager.equipment.repository;

import com.inframanager.equipment.persistence.entity.EquipmentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipmentRepository extends JpaRepository<EquipmentEntity, Long> {

    boolean existsByName(String name);

    Page<EquipmentEntity> findByNameContainingIgnoreCaseOrManufacturerContainingIgnoreCase(
            String name,
            String manufacturer,
            Pageable pageable
    );
}