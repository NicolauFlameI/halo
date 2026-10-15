package br.com.nicolas.halo.parsing;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

/**
 * Gera o "fingerprint" (impressão digital) de uma mensagem de log.
 *
 * <p>Objetivo: erros que diferem apenas em partes variáveis (ids, números, IPs, UUIDs)
 * devem produzir o MESMO fingerprint, para que o HALO consiga agrupá-los.
 * Exemplo: "Timeout apos 3000ms no pedido 8841" e "Timeout apos 5000ms no pedido 9120"
 * geram o mesmo hash.</p>
 */
@Component
public class FingerprintGenerator {

    // UUID no formato 8-4-4-4-12 (ex.: 123e4567-e89b-12d3-a456-426614174000)
    private static final Pattern UUID = Pattern.compile(
            "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}");

    // Endereço IPv4 (ex.: 10.0.0.15)
    private static final Pattern IPV4 = Pattern.compile("\\b\\d{1,3}(\\.\\d{1,3}){3}\\b");

    // Números hexadecimais com prefixo 0x (ex.: 0x7fff1a2b)
    private static final Pattern HEX = Pattern.compile("0x[0-9a-fA-F]+");

    // Qualquer sequência de dígitos (ids, tempos, portas, contadores...)
    private static final Pattern NUMBER = Pattern.compile("\\d+");

    // Espaços repetidos viram um só, para não gerar hashes diferentes por formatação
    private static final Pattern SPACES = Pattern.compile("\\s+");

    /**
     * Normaliza a mensagem trocando partes variáveis por marcadores fixos.
     * A ordem importa: UUID e IP precisam ser tratados antes dos números simples,
     * senão os dígitos deles seriam substituídos primeiro e o padrão não casaria mais.
     */
    public String normalize(String message) {
        if (message == null) {
            return "";
        }
        String result = UUID.matcher(message).replaceAll("<uuid>");
        result = IPV4.matcher(result).replaceAll("<ip>");
        result = HEX.matcher(result).replaceAll("<hex>");
        result = NUMBER.matcher(result).replaceAll("<n>");
        result = SPACES.matcher(result).replaceAll(" ");
        return result.trim().toLowerCase();
    }

    /** Calcula o SHA-256 (64 caracteres hexadecimais) da mensagem já normalizada. */
    public String generate(String message) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(normalize(message).getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            // SHA-256 é obrigatório em toda JVM; esse caso não deve acontecer na prática.
            throw new IllegalStateException("SHA-256 indisponível na JVM", e);
        }
    }
}
