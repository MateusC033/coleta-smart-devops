package com.fiap.residuos.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CaminhaoRequestDTO {

    @NotBlank(message = "Placa é obrigatória")
    @Size(min = 7, max = 10, message = "Placa inválida")
    private String placa;

    @NotNull(message = "Capacidade é obrigatória")
    @Positive(message = "Capacidade deve ser positiva")
    private Double capacidade;

    private Long motoristaId;
}
