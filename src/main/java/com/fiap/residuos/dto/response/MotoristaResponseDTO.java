package com.fiap.residuos.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class MotoristaResponseDTO {
    private Long id;
    private String nome;
    private String email;
    private String telefone;
}
