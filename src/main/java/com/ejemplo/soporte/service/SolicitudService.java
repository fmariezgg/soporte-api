package com.ejemplo.soporte.service;

import com.ejemplo.soporte.dto.NuevaSolicitudRequest;
import com.ejemplo.soporte.dto.Solicitud;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Service;

/** Holds the in-memory store. Thread-safe: ids come from an AtomicLong, list is copy-on-write. */
@Service
public class SolicitudService {

    private static final String INITIAL_STATE = "Pendiente";

    private final List<Solicitud> store = new CopyOnWriteArrayList<>();
    private final AtomicLong sequence = new AtomicLong(2);

    public SolicitudService() {
        store.add(new Solicitud(1, "Equipo no enciende", "Alta", "Pendiente"));
        store.add(new Solicitud(2, "Instalar software", "Media", "En proceso"));
    }

    public List<Solicitud> findAll() {
        return List.copyOf(store);
    }

    public Solicitud create(NuevaSolicitudRequest request) {
        Solicitud created = new Solicitud(
                sequence.incrementAndGet(), request.titulo().trim(), request.prioridad(), INITIAL_STATE);
        store.add(created);
        return created;
    }
}
