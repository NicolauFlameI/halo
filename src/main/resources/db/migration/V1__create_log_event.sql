CREATE TABLE log_event (
    id          BIGSERIAL    PRIMARY KEY,
    timestamp   TIMESTAMPTZ  NOT NULL,
    application VARCHAR(100) NOT NULL,
    level       VARCHAR(10)  NOT NULL,
    message     TEXT         NOT NULL,
    stack_trace TEXT,
    source_file VARCHAR(500),
    raw         TEXT,
    fingerprint VARCHAR(64),
    created_at  TIMESTAMPTZ  NOT NULL DEFAULT now()
);

CREATE INDEX idx_log_event_timestamp   ON log_event (timestamp);
CREATE INDEX idx_log_event_application ON log_event (application);
CREATE INDEX idx_log_event_level       ON log_event (level);
CREATE INDEX idx_log_event_fingerprint ON log_event (fingerprint);
