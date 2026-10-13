package com.fiap.residuos;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Smoke test: valida que o contexto Spring sobe inteiro com o perfil 'test'.
 * Pega erros comuns de configuração — beans faltando, binding de propriedades,
 * schema do Hibernate — antes de qualquer teste mais específico rodar.
 */
@SpringBootTest
@ActiveProfiles("test")
class ColetaSmartApplicationTests {

    @Test
    void contextLoads() {
        // Se o contexto não carregar, o próprio @SpringBootTest falha.
    }
}
