package br.com.nicolas.halo.ingestion;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.nicolas.halo.ingestion.dto.CreateLogEventRequest;
import br.com.nicolas.halo.ingestion.dto.LogEventResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

/**
 * API HTTP de ingestão de eventos de log.
 *
 * <ul>
 *   <li>POST /api/events        - recebe um evento</li>
 *   <li>POST /api/events/batch  - recebe uma lista de eventos</li>
 *   <li>GET  /api/events        - lista eventos (paginado, mais recentes primeiro)</li>
 * </ul>
 *
 * Requisições inválidas (campo obrigatório ausente, nível inexistente) retornam 400 automaticamente.
 */
@RestController
@RequestMapping("/api/events")
@Validated // habilita a validação dos parâmetros do método (usada no lote, via @NotEmpty)
public class LogEventController {

    private final LogEventService service;

    public LogEventController(LogEventService service) {
        this.service = service;
    }

    /** Recebe um evento e responde 201 Created com o evento salvo (incluindo id e fingerprint). */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LogEventResponse create(@Valid @RequestBody CreateLogEventRequest request) {
        return service.ingest(request);
    }

    /** Recebe vários eventos de uma vez. Cada item da lista também é validado (@Valid). */
    @PostMapping("/batch")
    @ResponseStatus(HttpStatus.CREATED)
    public List<LogEventResponse> createBatch(
            @RequestBody @NotEmpty List<@Valid CreateLogEventRequest> requests) {
        return service.ingestBatch(requests);
    }

    /** Lista os eventos; por padrão 20 por página, do mais recente para o mais antigo. */
    @GetMapping
    public Page<LogEventResponse> list(
            @PageableDefault(size = 20, sort = "timestamp", direction = Sort.Direction.DESC) Pageable pageable) {
        return service.list(pageable);
    }
}
