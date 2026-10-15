package br.com.nicolas.halo.parsing;

import java.time.Instant;

import br.com.nicolas.halo.event.LogLevel;

/**
 * Resultado do parsing de um evento de log em texto, já separado em campos.
 * Ainda não é persistido: serve de ponte entre o texto bruto e o contrato de ingestão.
 *
 * @param timestamp   momento do evento
 * @param application aplicação que gerou o log
 * @param level       nível de severidade
 * @param message     mensagem da linha principal
 * @param stackTrace  linhas de continuação (stack trace), ou {@code null} se não houver
 * @param raw         texto original do evento, incluindo as linhas de continuação
 */
public record ParsedLogEvent(
        Instant timestamp,
        String application,
        LogLevel level,
        String message,
        String stackTrace,
        String raw) {
}
