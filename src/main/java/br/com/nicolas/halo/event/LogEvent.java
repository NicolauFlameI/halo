package br.com.nicolas.halo.event;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "log_event")
public class LogEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Instant timestamp;

    @Column(nullable = false, length = 100)
    private String application;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private LogLevel level;

    @Column(nullable = false, columnDefinition = "text")
    private String message;

    @Column(name = "stack_trace", columnDefinition = "text")
    private String stackTrace;

    @Column(name = "source_file", length = 500)
    private String sourceFile;

    @Column(columnDefinition = "text")
    private String raw;

    /** Hash da mensagem normalizada, usado para agrupar erros semelhantes. */
    @Column(length = 64)
    private String fingerprint;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    protected LogEvent() {
    }

    public LogEvent(Instant timestamp, String application, LogLevel level, String message) {
        this.timestamp = timestamp;
        this.application = application;
        this.level = level;
        this.message = message;
    }

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
