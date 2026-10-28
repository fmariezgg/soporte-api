package com.ejemplo.soporte.service;

import com.ejemplo.soporte.dto.NuevaSolicitudRequest;
import com.ejemplo.soporte.dto.Solicitud;
import com.ejemplo.soporte.error.SolicitudNotFoundException;
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

    public Solicitud findById(long id) {
        return store.stream()
                .filter(s -> s.id() == id)
                .findFirst()
                .orElseThrow(() -> new SolicitudNotFoundException(id));
    }

    /** Replaces the element in place (same id, same position). Synchronized so concurrent updates cannot interleave. */
    public synchronized Solicitud updateEstado(long id, String estado) {
        for (int i = 0; i < store.size(); i++) {
            Solicitud current = store.get(i);
            if (current.id() == id) {
                Solicitud updated = new Solicitud(id, current.titulo(), current.prioridad(), estado);
                store.set(i, updated);
                return updated;
            }
        }
        throw new SolicitudNotFoundException(id);
    }
}
