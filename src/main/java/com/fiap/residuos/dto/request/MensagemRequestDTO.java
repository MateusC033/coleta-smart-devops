package com.fiap.residuos.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MensagemRequestDTO {

    @NotBlank(message = "Conteúdo é obrigatório")
    @Size(max = 1000)
    private String conteudo;

    private Long rotaId;
}
