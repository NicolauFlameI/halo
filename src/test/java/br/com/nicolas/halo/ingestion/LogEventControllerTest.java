package br.com.nicolas.halo.ingestion;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import br.com.nicolas.halo.event.LogLevel;
import br.com.nicolas.halo.ingestion.dto.LogEventResponse;

/**
 * Testa apenas a camada web (JSON, validação e códigos HTTP).
 * O service é simulado (mock), então não é necessário banco de dados.
 */
@WebMvcTest(LogEventController.class)
class LogEventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LogEventService service;

    @Test
    void eventoValidoRetorna201() throws Exception {
        when(service.ingest(any())).thenReturn(new LogEventResponse(
                1L, Instant.parse("2026-10-14T21:30:00Z"), "pedido-api",
                LogLevel.ERROR, "Timeout", null, null, "abc"));

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"application":"pedido-api","level":"ERROR","message":"Timeout"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.fingerprint").value("abc"));
    }

    @Test
    void semMensagemRetorna400() throws Exception {
        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"application":"pedido-api","level":"ERROR"}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void nivelInexistenteRetorna400() throws Exception {
        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"application":"pedido-api","level":"BANANA","message":"x"}
                                """))
                .andExpect(status().isBadRequest());
    }
}
