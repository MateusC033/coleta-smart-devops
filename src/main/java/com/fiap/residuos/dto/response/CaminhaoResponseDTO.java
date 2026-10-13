package com.fiap.residuos.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CaminhaoResponseDTO {
    private Long id;
    private String placa;
    private Double capacidade;
    private Long motoristaId;
    private String motoristaNome;
}
