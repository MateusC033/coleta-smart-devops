package com.fiap.residuos.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class RotaResponseDTO {
    private Long id;
    private String nome;
    private String bairrosAtendidos;
    private String diasSemana;
    private String turno;
    private Long caminhaoId;
    private String caminhaoPlaca;
    private String motoristaNome;
}
