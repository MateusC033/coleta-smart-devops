package com.fiap.residuos.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TB_ROTA")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Rota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NOME", nullable = false, length = 100)
    private String nome;

    @Column(name = "BAIRROS_ATENDIDOS", nullable = false, length = 500)
    private String bairrosAtendidos;

    @Column(name = "DIAS_SEMANA", nullable = false, length = 100)
    private String diasSemana;

    @Column(name = "TURNO", nullable = false, length = 20)
    private String turno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CAMINHAO_ID", nullable = false)
    private Caminhao caminhao;
}
