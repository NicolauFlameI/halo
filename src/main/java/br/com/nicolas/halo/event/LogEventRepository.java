package br.com.nicolas.halo.event;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Acesso ao banco para {@link LogEvent}.
 * Ao estender {@link JpaRepository}, o Spring Data gera automaticamente
 * save, findById, findAll (com paginação), delete etc. Consultas específicas
 * (por nível, aplicação, período, fingerprint) serão adicionadas aqui depois.
 */
public interface LogEventRepository extends JpaRepository<LogEvent, Long> {
}
