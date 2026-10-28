package com.ejemplo.soporte.error;

/** Thrown when a Solicitud id does not exist. Mapped to 404 by {@link ApiExceptionHandler}. */
public class SolicitudNotFoundException extends RuntimeException {

    public SolicitudNotFoundException(long id) {
        super("Solicitud " + id + " not found");
    }
}
