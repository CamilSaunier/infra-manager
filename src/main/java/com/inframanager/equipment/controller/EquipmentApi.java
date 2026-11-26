package com.inframanager.equipment.controller;

import com.inframanager.equipment.dto.EquipmentRequest;
import com.inframanager.equipment.dto.EquipmentResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface EquipmentApi {

    // GET ALL EQUIPMENT
    @Operation(
            summary = "Get all equipment",
            description = "Retrieve equipment with optional search and pagination."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Equipment retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid parameters")
    })
    Page<EquipmentResponse> getEquipment(
            String search,
            Pageable pageable
    );

    // GET ONE EQUIPMENT BY ID
    @Operation(
            summary = "Get one equipment by ID",
            description = "Retrieve one equipment by its ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Equipment retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Equipment not found")
    })
    EquipmentResponse getEquipmentById(Long id);

    // CREATE ONE EQUIPMENT
    @Operation(
            summary = "Create equipment",
            description = "Create a new equipment."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Equipment created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid equipment data"),
            @ApiResponse(responseCode = "409", description = "Equipment name already exists")
    })
    EquipmentResponse createEquipment(EquipmentRequest equipmentBody);

    // UPDATE EQUIPMENT
    @Operation(
            summary = "Update equipment",
            description = "Update an existing equipment by its ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Equipment updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid equipment data"),
            @ApiResponse(responseCode = "404", description = "Equipment not found"),
            @ApiResponse(responseCode = "409", description = "Equipment name already exists")
    })
    EquipmentResponse updateEquipment(Long id, EquipmentRequest equipmentBody);

    // DELETE EQUIPMENT
    @Operation(
            summary = "Delete equipment",
            description = "Delete an existing equipment by its ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Equipment deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Equipment not found")
    })
    ResponseEntity<Void> deleteEquipment(Long id);

}
