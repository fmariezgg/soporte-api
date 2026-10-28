package com.ejemplo.soporte.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/** Body of POST /api/solicitudes. */
public record NuevaSolicitudRequest(
        @NotBlank(message = "titulo is required")
        @Size(max = 120, message = "titulo must have at most 120 characters")
        String titulo,

        @NotNull(message = "prioridad is required")
        @Pattern(regexp = "Alta|Media|Baja", message = "prioridad must be one of: Alta, Media, Baja")
        String prioridad) {
}
