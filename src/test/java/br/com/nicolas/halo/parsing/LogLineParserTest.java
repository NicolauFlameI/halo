package br.com.nicolas.halo.parsing;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;

import org.junit.jupiter.api.Test;

import br.com.nicolas.halo.event.LogLevel;

class LogLineParserTest {

    private static final ZoneId SP = ZoneId.of("America/Sao_Paulo");

    private final LogLineParser parser = new LogLineParser();

    @Test
    void parsesSimpleLineUsingGivenZone() {
        var event = parser.parseLine(
                "2026-10-14 22:47:00.123 ERROR [pedido-api] Timeout apos 3000ms ao chamar estoque", SP).orElseThrow();

        assertThat(event.timestamp()).isEqualTo(Instant.parse("2026-10-15T01:47:00.123Z"));
        assertThat(event.level()).isEqualTo(LogLevel.ERROR);
        assertThat(event.application()).isEqualTo("pedido-api");
        assertThat(event.message()).isEqualTo("Timeout apos 3000ms ao chamar estoque");
        assertThat(event.stackTrace()).isNull();
    }

    @Test
    void respectsExplicitOffsetAndIsoSeparator() {
        var event = parser.parseLine("2026-10-14T22:47:00-03:00 INFO [api] ok", ZoneId.of("UTC")).orElseThrow();

        assertThat(event.timestamp()).isEqualTo(Instant.parse("2026-10-15T01:47:00Z"));
    }

    @Test
    void mapsWarningToWarnAndAcceptsMissingMillis() {
        var event = parser.parseLine("2026-10-14 22:47:00 WARNING [api] lento", SP).orElseThrow();

        assertThat(event.level()).isEqualTo(LogLevel.WARN);
    }

    @Test
    void rejectsLinesWithoutValidHeader() {
        assertThat(parser.parseLine("    at com.foo.Bar.baz(Bar.java:10)")).isEmpty();
        assertThat(parser.parseLine("2026-10-14 22:47:00 BANANA [api] x")).isEmpty();
        assertThat(parser.parseLine("2026-13-45 22:47:00 INFO [api] x")).isEmpty();
        assertThat(parser.parseLine("")).isEmpty();
        assertThat(parser.parseLine(null)).isEmpty();
    }

    @Test
    void attachesContinuationLinesAsStackTrace() {
        List<String> lines = List.of(
                "ignorada antes do primeiro cabecalho",
                "2026-10-14 22:47:00 INFO [api] iniciado",
                "2026-10-14 22:47:01 ERROR [api] Deadlock detectado",
                "org.postgresql.util.PSQLException: deadlock detected",
                "    at com.foo.Dao.update(Dao.java:42)",
                "Caused by: java.sql.SQLException: lock",
                "",
                "2026-10-14 22:47:02 WARN [gateway] latencia alta");

        var events = parser.parse(lines, SP);

        assertThat(events).hasSize(3);
        assertThat(events.get(0).stackTrace()).isNull();
        assertThat(events.get(1).message()).isEqualTo("Deadlock detectado");
        assertThat(events.get(1).stackTrace()).isEqualTo(
                "org.postgresql.util.PSQLException: deadlock detected\n"
                        + "    at com.foo.Dao.update(Dao.java:42)\n"
                        + "Caused by: java.sql.SQLException: lock");
        assertThat(events.get(1).raw()).startsWith("2026-10-14 22:47:01 ERROR").contains("Dao.update");
        assertThat(events.get(2).application()).isEqualTo("gateway");
    }

    @Test
    void emptyInputGivesNoEvents() {
        assertThat(parser.parse(List.of())).isEmpty();
    }
}
