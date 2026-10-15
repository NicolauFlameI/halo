package br.com.nicolas.halo.ingestion.dto;

import java.time.Instant;

import br.com.nicolas.halo.event.LogLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Contrato de entrada para ingestão de um evento de log (corpo do POST /api/events).
 * É este formato que o LogLab (e qualquer outra fonte) deve enviar ao HALO.
 *
 * <p>Exemplo de JSON:</p>
 * <pre>
 * {
 *   "timestamp": "2026-10-14T21:30:00Z",
 *   "application": "pedido-api",
 *   "level": "ERROR",
 *   "message": "Timeout apos 3000ms ao chamar servico de estoque",
 *   "stackTrace": "java.net.SocketTimeoutException: ...",
 *   "sourceFile": "pedido-api.log"
 * }
 * </pre>
 *
 * @param timestamp   momento em que o evento ocorreu (opcional; se ausente, usa a hora de recebimento)
 * @param application nome da aplicação que gerou o log (obrigatório)
 * @param level       nível do log: TRACE, DEBUG, INFO, WARN, ERROR ou FATAL (obrigatório)
 * @param message     mensagem principal do log (obrigatória)
 * @param stackTrace  stack trace completo, quando houver exceção (opcional)
 * @param sourceFile  arquivo de origem do log (opcional)
 * @param raw         linha original, sem processamento (opcional)
 */
public record CreateLogEventRequest(
        Instant timestamp,
        @NotBlank @Size(max = 100) String application,
        @NotNull LogLevel level,
        @NotBlank String message,
        String stackTrace,
        @Size(max = 500) String sourceFile,
        String raw) {
}
