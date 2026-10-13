package com.fiap.residuos.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class MensagemResponseDTO {
    private Long id;
    private String remetenteNome;
    private String rotaNome;
    private String conteudo;
    private LocalDateTime dataHora;
    private String tipo;
}
