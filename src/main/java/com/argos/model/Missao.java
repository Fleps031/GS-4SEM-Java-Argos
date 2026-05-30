package com.argos.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TB_MISSAO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Missao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_MISSAO")
    private Long id;

    @Column(name = "NM_MISSAO", nullable = false, length = 100)
    private String nome;

    @Column(name = "DS_MISSAO", nullable = false, length = 500)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(name = "TP_STATUS", nullable = false)
    private StatusMissao status;

    @Enumerated(EnumType.STRING)
    @Column(name = "TP_RISCO", nullable = false)
    private NivelRisco nivelRisco;

    @Column(name = "DS_AREA", nullable = false, length = 150)
    private String areaOperacao;

    @Column(name = "DT_CRIACAO", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "DT_ATUALIZACAO")
    private LocalDateTime dataAtualizacao;

    @PrePersist
    protected void onCreate() {
        this.dataCriacao = LocalDateTime.now();
        this.dataAtualizacao = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.dataAtualizacao = LocalDateTime.now();
    }

}
