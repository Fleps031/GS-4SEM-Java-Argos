package com.argos.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TB_ALERTA")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Alerta {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_alerta")
    @SequenceGenerator(name = "seq_alerta", sequenceName = "SEQ_ALERTA", allocationSize = 1)
    @Column(name = "ID_ALERTA")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_MISSAO", nullable = false)
    private Missao missao;

    @Column(name = "DS_MENSAGEM", nullable = false, length = 300)
    private String mensagem;

    @Enumerated(EnumType.STRING)
    @Column(name = "TP_SEVERIDADE", nullable = false, length = 10)
    private Severidade severidade;

    @Column(name = "FL_RESOLVIDO", nullable = false)
    private Boolean resolvido = false;

    @Column(name = "DT_ALERTA", nullable = false, updatable = false)
    private LocalDateTime dataAlerta;

    @PrePersist
    public void prePersist() {
        this.dataAlerta = LocalDateTime.now();
        if (this.resolvido == null) this.resolvido = false;
    }
}