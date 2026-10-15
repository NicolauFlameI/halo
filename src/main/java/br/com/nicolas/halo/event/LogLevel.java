package br.com.nicolas.halo.event;

/**
 * Níveis de severidade de um log, do menos para o mais grave.
 * É gravado no banco como texto (EnumType.STRING), o que mantém os dados legíveis
 * e evita quebrar registros antigos caso a ordem do enum mude.
 */
public enum LogLevel {
    TRACE, DEBUG, INFO, WARN, ERROR, FATAL
}
