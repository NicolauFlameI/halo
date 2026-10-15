package br.com.nicolas.halo.event;

import jakarta.persistence.*;

import java.time.Instant;

/**
 * Um evento de log ingerido pelo HALO (uma linha da tabela {@code log_event}).
 * É a unidade básica de tudo: timeline, agrupamento de erros, correlação e incidentes
 * partem desses registros. O schema é criado pela migration V1 (Flyway).
 */
@Entity
@Table(name = "log_event")
public class LogEvent {

    // Chave primária gerada pelo banco (BIGSERIAL)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Momento em que o evento OCORREU na aplicação de origem (diferente de createdAt)
    @Column(nullable = false)
    private Instant timestamp;

    // Nome da aplicação/serviço que gerou o log (ex.: "pedido-api")
    @Column(nullable = false, length = 100)
    private String application;

    // Severidade, gravada como texto ("ERROR", "WARN"...)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private LogLevel level;

    // Mensagem principal do log; "text" permite tamanho ilimitado
    @Column(nullable = false, columnDefinition = "text")
    private String message;

    // Stack trace completo quando o log registra uma exceção
    @Column(name = "stack_trace", columnDefinition = "text")
    private String stackTrace;

    // Arquivo de log de onde o evento foi lido (útil para correlacionar arquivos)
    @Column(name = "source_file", length = 500)
    private String sourceFile;

    // Linha original, exatamente como veio, para auditoria e reprocessamento
    @Column(columnDefinition = "text")
    private String raw;

    /** Hash da mensagem normalizada, usado para agrupar erros semelhantes. */
    @Column(length = 64)
    private String fingerprint;

    // Momento em que o HALO recebeu/gravou o evento (não muda após criado)
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    /** Construtor sem argumentos exigido pelo JPA; protegido para não ser usado no código normal. */
    protected LogEvent() {
    }

    /** Construtor com os campos obrigatórios; os opcionais são definidos pelos setters. */
    public LogEvent(Instant timestamp, String application, LogLevel level, String message) {
        this.timestamp = timestamp;
        this.application = application;
        this.level = level;
        this.message = message;
    }

    // Getters/setters: id, timestamp, application, level, message e createdAt não têm setter
    // porque são definidos na criação e não devem mudar depois.
    public Long getId() { return id; }
    public Instant getTimestamp() { return timestamp; }
    public String getApplication() { return application; }
    public LogLevel getLevel() { return level; }
    public String getMessage() { return message; }
    public String getStackTrace() { return stackTrace; }
    public void setStackTrace(String stackTrace) { this.stackTrace = stackTrace; }
    public String getSourceFile() { return sourceFile; }
    public void setSourceFile(String sourceFile) { this.sourceFile = sourceFile; }
    public String getRaw() { return raw; }
    public void setRaw(String raw) { this.raw = raw; }
    public String getFingerprint() { return fingerprint; }
    public void setFingerprint(String fingerprint) { this.fingerprint = fingerprint; }
    public Instant getCreatedAt() { return createdAt; }
}
