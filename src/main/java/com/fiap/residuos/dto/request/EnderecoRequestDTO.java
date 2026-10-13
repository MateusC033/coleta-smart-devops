package com.fiap.residuos.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class EnderecoRequestDTO {

    @NotBlank(message = "CEP é obrigatório")
    @Size(max = 10)
    private String cep;

    @NotBlank(message = "Rua é obrigatória")
    @Size(max = 150)
    private String rua;

    @NotBlank(message = "Bairro é obrigatório")
    @Size(max = 100)
    private String bairro;

    @NotBlank(message = "Número é obrigatório")
    @Size(max = 10)
    private String numero;
}
