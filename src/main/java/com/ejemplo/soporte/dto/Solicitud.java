package com.ejemplo.soporte.dto;

/** Support request exposed by the API. Field names are part of the frontend contract. */
public record Solicitud(long id, String titulo, String prioridad, String estado) {
}
