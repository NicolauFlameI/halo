package br.com.nicolas.halo.parsing;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import br.com.nicolas.halo.event.LogLevel;

/**
 * Converte texto de log em {@link ParsedLogEvent}.
 *
 * <p>Formato esperado da linha de cabeçalho (é o formato que o LogLab deve gerar):</p>
 * <pre>
 * 2026-10-14 22:47:00.123 ERROR [pedido-api] Timeout apos 3000ms ao chamar estoque
 * </pre>
 * <ul>
 *   <li>Data e hora separadas por espaço ou {@code T}; milissegundos opcionais.</li>
 *   <li>Offset opcional ({@code Z}, {@code -03:00}); sem offset usa o fuso informado.</li>
 *   <li>Nível: TRACE, DEBUG, INFO, WARN (ou WARNING), ERROR ou FATAL.</li>
 *   <li>Aplicação entre colchetes.</li>
 * </ul>
 *
 * <p>Linhas que não começam com um cabeçalho válido (ex.: {@code at com.foo.Bar.baz(Bar.java:10)},
 * {@code Caused by: ...}) são tratadas como continuação do evento anterior e vão para o stack trace.</p>
 */
@Component
public class LogLineParser {

    private static final Pattern HEADER = Pattern.compile(
            "^(\\d{4}-\\d{2}-\\d{2})[T ](\\d{2}:\\d{2}:\\d{2})(?:[.,](\\d{1,9}))?"
                    + "\\s*(Z|[+-]\\d{2}:?\\d{2})?"
                    + "\\s+(TRACE|DEBUG|INFO|WARN|WARNING|ERROR|FATAL)"
                    + "\\s+\\[([^\\]]+)\\]\\s*(.*)$");

    /** Interpreta uma única linha; vazio se ela não for um cabeçalho de evento. Usa o fuso do sistema. */
    public Optional<ParsedLogEvent> parseLine(String line) {
        return parseLine(line, ZoneId.systemDefault());
    }

    public Optional<ParsedLogEvent> parseLine(String line, ZoneId zone) {
        if (line == null) {
            return Optional.empty();
        }
        Matcher m = HEADER.matcher(line.strip());
        if (!m.matches()) {
            return Optional.empty();
        }
        Instant timestamp;
        try {
            timestamp = toInstant(m, zone);
        } catch (DateTimeParseException e) {
            return Optional.empty();
        }
        return Optional.of(new ParsedLogEvent(
                timestamp, m.group(6).strip(), toLevel(m.group(5)), m.group(7).strip(), null, line));
    }

    /** Interpreta várias linhas, juntando as de continuação ao evento anterior. Usa o fuso do sistema. */
    public List<ParsedLogEvent> parse(List<String> lines) {
        return parse(lines, ZoneId.systemDefault());
    }

    public List<ParsedLogEvent> parse(List<String> lines, ZoneId zone) {
        List<ParsedLogEvent> events = new ArrayList<>();
        ParsedLogEvent current = null;
        StringBuilder stack = new StringBuilder();
        StringBuilder raw = new StringBuilder();

        for (String line : lines) {
            Optional<ParsedLogEvent> header = parseLine(line, zone);
            if (header.isPresent()) {
                if (current != null) {
                    events.add(finish(current, stack, raw));
                }
                current = header.get();
                stack.setLength(0);
                raw.setLength(0);
                raw.append(line);
            } else if (current != null && line != null && !line.isBlank()) {
                if (!stack.isEmpty()) {
                    stack.append('\n');
                }
                stack.append(line.stripTrailing());
                raw.append('\n').append(line);
            }
            // Linhas antes do primeiro cabeçalho não pertencem a nenhum evento e são ignoradas.
        }
        if (current != null) {
            events.add(finish(current, stack, raw));
        }
        return events;
    }

    private ParsedLogEvent finish(ParsedLogEvent header, StringBuilder stack, StringBuilder raw) {
        return new ParsedLogEvent(header.timestamp(), header.application(), header.level(),
                header.message(), stack.isEmpty() ? null : stack.toString(), raw.toString());
    }

    private Instant toInstant(Matcher m, ZoneId zone) {
        String fraction = m.group(3) == null ? "" : "." + m.group(3);
        String local = m.group(1) + "T" + m.group(2) + fraction;
        String offset = m.group(4);
        if (offset == null) {
            return LocalDateTime.parse(local).atZone(zone).toInstant();
        }
        if (!offset.equals("Z") && !offset.contains(":")) {
            offset = offset.substring(0, 3) + ":" + offset.substring(3);
        }
        return OffsetDateTime.parse(local + offset).toInstant();
    }

    private LogLevel toLevel(String text) {
        String upper = text.toUpperCase(Locale.ROOT);
        return upper.equals("WARNING") ? LogLevel.WARN : LogLevel.valueOf(upper);
    }
}
