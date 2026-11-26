package com.inframanager.exception;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Standard error response returned by the API")
public record ErrorResponse(

        @Schema(description = "HTTP status code", example = "400")
        int status,

        @Schema(description = "HTTP status description", example = "Bad Request")
        String error,

        @Schema(description = "Detailed error message", example = "Validation failed")
        String message,

        @Schema(
                description = "List of validation errors",
                example = "[\"name: must not be blank\", \"model: must not be blank\"]"
        )
        List<String> details

) {
}