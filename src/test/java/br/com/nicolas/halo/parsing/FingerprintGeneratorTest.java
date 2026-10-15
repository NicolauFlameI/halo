package br.com.nicolas.halo.parsing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

/** Testes unitários do gerador de fingerprint (não precisam de Spring nem de banco). */
class FingerprintGeneratorTest {

    private final FingerprintGenerator generator = new FingerprintGenerator();

    @Test
    void mensagensQueDiferemSoNosNumerosGeramMesmoFingerprint() {
        String a = generator.generate("Timeout apos 3000ms no pedido 8841");
        String b = generator.generate("Timeout apos 5000ms no pedido 9120");
        assertEquals(a, b);
    }

    @Test
    void mensagensDiferentesGeramFingerprintsDiferentes() {
        String a = generator.generate("Timeout ao chamar estoque");
        String b = generator.generate("Deadlock detectado na tabela pedido");
        assertNotEquals(a, b);
    }

    @Test
    void normalizaUuidEIp() {
        String normalized = generator.normalize(
                "Falha 123e4567-e89b-12d3-a456-426614174000 em 10.0.0.15");
        assertEquals("falha <uuid> em <ip>", normalized);
    }

    @Test
    void fingerprintTem64CaracteresHexadecimais() {
        assertEquals(64, generator.generate("qualquer mensagem").length());
    }
}
