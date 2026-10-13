package com.fiap.residuos.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TB_CAMINHAO")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Caminhao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "PLACA", nullable = false, unique = true, length = 10)
    private String placa;

    @Column(name = "CAPACIDADE", nullable = false)
    private Double capacidade;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MOTORISTA_ID")
    private Usuario motorista;
}
