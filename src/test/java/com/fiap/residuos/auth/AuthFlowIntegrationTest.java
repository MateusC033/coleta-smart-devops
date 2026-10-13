package com.fiap.residuos.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Testa o fluxo real de autenticação ponta a ponta:
 * cadastro de morador → login → acesso a endpoint protegido.
 * Usa H2 em memória (perfil 'test'), sem mocks de repositório.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthFlowIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void registrarRetornaCreatedComToken() throws Exception {
        String payload = objectMapper.writeValueAsString(moradorValido("maria@teste.com"));

        mockMvc.perform(post("/auth/registrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.tipo").value("Bearer"))
                .andExpect(jsonPath("$.role").value("ROLE_USER"));
    }

    @Test
    void loginRetornaOkComTokenAposCadastro() throws Exception {
        var dto = moradorValido("joao@teste.com");
        mockMvc.perform(post("/auth/registrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        String loginBody = objectMapper.writeValueAsString(Map.of(
                "email", "joao@teste.com",
                "senha", "senha123"
        ));

        MvcResult result = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        assertThat(responseJson).contains("Bearer");
    }

    @Test
    void endpointProtegidoSemTokenRetornaNaoAutorizado() throws Exception {
        // /api/rotas/meu-bairro exige role USER — sem token, filtro JWT devolve 401/403.
        mockMvc.perform(get("/api/rotas/meu-bairro"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void registrarComEmailInvalidoRetornaBadRequest() throws Exception {
        var dto = moradorValido("isso-nao-eh-email");
        mockMvc.perform(post("/auth/registrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    private Map<String, Object> moradorValido(String email) {
        return Map.of(
                "nome", "Morador Teste",
                "email", email,
                "senha", "senha123",
                "telefone", "11999999999",
                "endereco", Map.of(
                        "cep", "01310-100",
                        "rua", "Av. Paulista",
                        "bairro", "Bela Vista",
                        "numero", "1000"
                )
        );
    }
}
