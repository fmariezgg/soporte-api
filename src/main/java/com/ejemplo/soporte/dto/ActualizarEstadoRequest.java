package com.ejemplo.soporte.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/** Body of PATCH /api/solicitudes/{id}. */
public record ActualizarEstadoRequest(
        @NotBlank(message = "estado is required")
        @Pattern(regexp = "Pendiente|En proceso|Resuelta",
                message = "estado must be one of: Pendiente, En proceso, Resuelta")
        String estado) {
}
