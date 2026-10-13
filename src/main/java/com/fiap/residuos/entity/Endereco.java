package com.fiap.residuos.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TB_ENDERECO")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "CEP", nullable = false, length = 10)
    private String cep;

    @Column(name = "RUA", nullable = false, length = 150)
    private String rua;

    @Column(name = "BAIRRO", nullable = false, length = 100)
    private String bairro;

    @Column(name = "NUMERO", nullable = false, length = 10)
    private String numero;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USUARIO_ID", nullable = false)
    private Usuario usuario;
}
