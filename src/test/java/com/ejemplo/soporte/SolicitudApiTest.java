package com.ejemplo.soporte;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class SolicitudApiTest {

    private static final String ORIGIN = "http://localhost:5173";

    @Autowired
    private MockMvc mvc;

    @Test
    void getReturnsSeeds() throws Exception {
        mvc.perform(get("/api/solicitudes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].titulo", is("Equipo no enciende")))
                .andExpect(jsonPath("$[0].prioridad", is("Alta")))
                .andExpect(jsonPath("$[0].estado", is("Pendiente")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].estado", is("En proceso")));
    }

    @Test
    void postValidReturns201WithLocationAndPendiente() throws Exception {
        mvc.perform(post("/api/solicitudes").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"titulo\":\"  Proyector roto \",\"prioridad\":\"Baja\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/api/solicitudes/")))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.titulo", is("Proyector roto")))
                .andExpect(jsonPath("$.prioridad", is("Baja")))
                .andExpect(jsonPath("$.estado", is("Pendiente")));
    }

    @Test
    void postBlankTituloReturns400() throws Exception {
        mvc.perform(post("/api/solicitudes").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"titulo\":\"   \",\"prioridad\":\"Alta\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("Bad Request")))
                .andExpect(jsonPath("$.messages", hasSize(1)));
    }

    @Test
    void postTituloTooLongReturns400() throws Exception {
        String longTitle = "a".repeat(121);
        mvc.perform(post("/api/solicitudes").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"titulo\":\"" + longTitle + "\",\"prioridad\":\"Alta\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void postInvalidPrioridadReturns400() throws Exception {
        mvc.perform(post("/api/solicitudes").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"titulo\":\"Algo\",\"prioridad\":\"Urgente\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.messages[0]", containsString("prioridad")));
    }

    @Test
    void postMalformedJsonReturns400() throws Exception {
        mvc.perform(post("/api/solicitudes").contentType(MediaType.APPLICATION_JSON).content("{oops"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)));
    }

    @Test
    void preflightFromAllowedOriginIsAccepted() throws Exception {
        mvc.perform(options("/api/solicitudes")
                        .header("Origin", ORIGIN)
                        .header("Access-Control-Request-Method", "POST")
                        .header("Access-Control-Request-Headers", "content-type"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", ORIGIN));
    }

    @Test
    void getWithAllowedOriginExposesCorsHeader() throws Exception {
        mvc.perform(get("/api/solicitudes").header("Origin", ORIGIN))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", ORIGIN));
    }

    @Test
    void disallowedOriginIsRejected() throws Exception {
        mvc.perform(get("/api/solicitudes").header("Origin", "http://evil.example"))
                .andExpect(status().isForbidden());
    }
}
