/**
 * Pacote raiz do HALO. Subpacotes por domínio:
 * <ul>
 *   <li>{@code ingestion} - entrada de eventos (API HTTP, depois leitura de arquivos e tail)</li>
 *   <li>{@code parsing}   - interpretação de logs (fingerprint, depois parsers de formato)</li>
 *   <li>{@code event}     - entidade e repositório de eventos de log</li>
 *   <li>{@code analysis}  - agrupamento, correlação e timeline (a implementar)</li>
 *   <li>{@code incident}  - histórico de incidentes (a implementar)</li>
 *   <li>{@code ai}        - integração com IA/Ollama (a implementar)</li>
 * </ul>
 */
package br.com.nicolas.halo;
