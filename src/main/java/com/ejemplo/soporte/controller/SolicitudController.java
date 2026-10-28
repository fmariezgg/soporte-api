package com.ejemplo.soporte.controller;

import com.ejemplo.soporte.dto.NuevaSolicitudRequest;
import com.ejemplo.soporte.dto.Solicitud;
import com.ejemplo.soporte.service.SolicitudService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudController {

    private final SolicitudService service;

    public SolicitudController(SolicitudService service) {
        this.service = service;
    }

    @GetMapping
    public List<Solicitud> list() {
        return service.findAll();
    }

    @PostMapping
    public ResponseEntity<Solicitud> create(@Valid @RequestBody NuevaSolicitudRequest request) {
        Solicitud created = service.create(request);
        // No GET-by-id endpoint in the contract, so Location points at the collection with the new id.
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(created.id()).toUri();
        return ResponseEntity.created(location).body(created);
    }
}
