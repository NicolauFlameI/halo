package br.com.nicolas.halo.ingestion.dto;

import java.time.Instant;

import br.com.nicolas.halo.event.LogEvent;
import br.com.nicolas.halo.event.LogLevel;

/**
 * Representação de um evento de log devolvida pela API.
 * Existe para não expor a entidade JPA diretamente (desacopla API e banco).
 */
public record LogEventResponse(
        Long id,
        Instant timestamp,
        String application,
        LogLevel level,
        String message,
        String stackTrace,
        String sourceFile,
        String fingerprint) {

    /** Converte a entidade persistida no DTO de resposta. */
    public static LogEventResponse from(LogEvent event) {
        return new LogEventResponse(
                event.getId(),
                event.getTimestamp(),
                event.getApplication(),
                event.getLevel(),
                event.getMessage(),
                event.getStackTrace(),
                event.getSourceFile(),
                event.getFingerprint());
    }
}
