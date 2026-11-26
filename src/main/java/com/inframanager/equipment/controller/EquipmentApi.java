
package com.inframanager.equipment.controller;

import com.inframanager.equipment.dto.EquipmentRequest;
import com.inframanager.equipment.dto.EquipmentResponse;
import com.inframanager.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
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
            @ApiResponse(
                    responseCode = "200",
                    description = "Equipment retrieved successfully",
                    content = @Content(
                            schema = @Schema(implementation = EquipmentResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid parameters",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "Invalid parameters",
                                    value = """
                                            {
                                              "status": 400,
                                              "error": "Bad Request",
                                              "message": "Invalid pagination parameters",
                                              "details": []
                                            }
                                            """
                            )
                    )
            )
    })
    Page<EquipmentResponse> getEquipment(
            @Parameter(
                    description = "Search by equipment name, manufacturer or model",
                    example = "Cisco"
            )
            String search,
            @Parameter(hidden = true)
            Pageable pageable
    );

    // GET EQUIPMENT BY ID
    @Operation(
            summary = "Get equipment by ID",
            description = "Retrieve an equipment by its unique identifier."
    )

    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Equipment retrieved successfully",
                    content = @Content(
                            schema = @Schema(implementation = EquipmentResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Equipment not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "Not found error",
                                    value = """
                                            {
                                              "status": 404,
                                              "error": "Not Found",
                                              "message": "Equipment not found",
                                              "details": []
                                            }
                                            """
                            )
                    )
            )
    })
    EquipmentResponse getEquipmentById(
            @Parameter(
                    description = "Unique identifier of the equipment",
                    example = "42"
            )
            Long id
    );

    // CREATE EQUIPMENT
    @Operation(
            summary = "Create equipment",
            description = "Create a new equipment."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Equipment created successfully",
                    content = @Content(
                            schema = @Schema(implementation = EquipmentResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid equipment data",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "Validation error",
                                    value = """
                                            {
                                              "status": 400,
                                              "error": "Bad Request",
                                              "message": "Validation failed",
                                              "details": [
                                                "name: must not be blank",
                                                "model: must not be blank"
                                              ]
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Equipment name already exists",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "Conflict error",
                                    value = """
                                            {
                                              "status": 409,
                                              "error": "Conflict",
                                              "message": "Equipment name already exists",
                                              "details": []
                                            }
                                            """
                            )
                    )
            )
    })
    EquipmentResponse createEquipment(EquipmentRequest equipmentBody);

    // UPDATE EQUIPMENT
    @Operation(
            summary = "Update equipment",
            description = "Update an existing equipment by its ID."
    )

    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Equipment updated successfully",
                    content = @Content(
                            schema = @Schema(implementation = EquipmentResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid equipment data",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "Validation error",
                                    value = """
                                            {
                                              "status": 400,
                                              "error": "Bad Request",
                                              "message": "Validation failed",
                                              "details": [
                                                "name: must not be blank"
                                              ]
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Equipment not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "Not found error",
                                    value = """
                                            {
                                              "status": 404,
                                              "error": "Not Found",
                                              "message": "Equipment not found",
                                              "details": []
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Equipment name already exists",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "Conflict error",
                                    value = """
                                            {
                                              "status": 409,
                                              "error": "Conflict",
                                              "message": "Equipment name already exists",
                                              "details": []
                                            }
                                            """
                            )
                    )
            )
    })
    EquipmentResponse updateEquipment(
            @Parameter(
                    description = "Unique identifier of the equipment",
                    example = "42"
            )
            Long id,
            EquipmentRequest equipmentBody
    );

    // DELETE EQUIPMENT
    @Operation(
            summary = "Delete equipment",
            description = "Delete an existing equipment by its ID."
    )

    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Equipment deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Equipment not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "Not found error",
                                    value = """
                                            {
                                              "status": 404,
                                              "error": "Not Found",
                                              "message": "Equipment not found",
                                              "details": []
                                            }
                                            """
                            )
                    )
            )
    })
    ResponseEntity<Void> deleteEquipment(
            @Parameter(
                    description = "Unique identifier of the equipment",
                    example = "42"
            )
            Long id
    );
}
