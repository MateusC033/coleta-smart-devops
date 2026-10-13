package com.fiap.residuos.entity;

import com.fiap.residuos.enums.TipoMensagem;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "TB_MENSAGEM")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Mensagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REMETENTE_ID", nullable = false)
    private Usuario remetente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ROTA_ID")
    private Rota rota;

    @Column(name = "CONTEUDO", nullable = false, length = 1000)
    private String conteudo;

    @Column(name = "DATA_HORA", nullable = false)
    private LocalDateTime dataHora;

    @Enumerated(EnumType.STRING)
    @Column(name = "TIPO", nullable = false, length = 30)
    private TipoMensagem tipo;
}
