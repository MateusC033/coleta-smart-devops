package com.fiap.residuos.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenResponseDTO {
    private String token;
    private String tipo;
    private String nome;
    private String role;
}
