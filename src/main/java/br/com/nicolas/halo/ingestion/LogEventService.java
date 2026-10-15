package br.com.nicolas.halo.ingestion;

import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.nicolas.halo.event.LogEvent;
import br.com.nicolas.halo.event.LogEventRepository;
import br.com.nicolas.halo.ingestion.dto.CreateLogEventRequest;
import br.com.nicolas.halo.ingestion.dto.LogEventResponse;
import br.com.nicolas.halo.parsing.FingerprintGenerator;

/**
 * Regras de negócio da ingestão: converte a requisição em entidade,
 * completa os campos derivados (timestamp padrão e fingerprint) e persiste.
 */
@Service
public class LogEventService {

    private final LogEventRepository repository;
    private final FingerprintGenerator fingerprintGenerator;

    public LogEventService(LogEventRepository repository, FingerprintGenerator fingerprintGenerator) {
        this.repository = repository;
        this.fingerprintGenerator = fingerprintGenerator;
    }

    /** Ingere um único evento. */
    @Transactional
    public LogEventResponse ingest(CreateLogEventRequest request) {
        return LogEventResponse.from(repository.save(toEntity(request)));
    }

    /**
     * Ingere vários eventos de uma vez, em uma única transação
     * (tudo é salvo ou nada é salvo). Útil para o LogLab enviar lotes.
     */
    @Transactional
    public List<LogEventResponse> ingestBatch(List<CreateLogEventRequest> requests) {
        List<LogEvent> entities = requests.stream().map(this::toEntity).toList();
        return repository.saveAll(entities).stream().map(LogEventResponse::from).toList();
    }

    /** Lista eventos de forma paginada (a ordenação vem do Pageable). */
    @Transactional(readOnly = true)
    public Page<LogEventResponse> list(Pageable pageable) {
        return repository.findAll(pageable).map(LogEventResponse::from);
    }

    /** Monta a entidade a partir do DTO, preenchendo timestamp e fingerprint. */
    private LogEvent toEntity(CreateLogEventRequest request) {
        // Se a fonte não informou quando o evento ocorreu, usamos o momento do recebimento.
        Instant timestamp = request.timestamp() != null ? request.timestamp() : Instant.now();

        LogEvent event = new LogEvent(timestamp, request.application(), request.level(), request.message());
        event.setStackTrace(request.stackTrace());
        event.setSourceFile(request.sourceFile());
        event.setRaw(request.raw());
        // O fingerprint é calculado a partir da mensagem para permitir agrupar erros semelhantes.
        event.setFingerprint(fingerprintGenerator.generate(request.message()));
        return event;
    }
}
