package com.fiap.residuos.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RotaRequestDTO {

    @NotBlank(message = "Nome da rota é obrigatório")
    @Size(max = 100)
    private String nome;

    @NotBlank(message = "Bairros atendidos são obrigatórios")
    @Size(max = 500)
    private String bairrosAtendidos;

    @NotBlank(message = "Dias da semana são obrigatórios")
    @Size(max = 100)
    private String diasSemana;

    @NotBlank(message = "Turno é obrigatório")
    @Size(max = 20)
    private String turno;

    @NotNull(message = "ID do caminhão é obrigatório")
    private Long caminhaoId;
}
