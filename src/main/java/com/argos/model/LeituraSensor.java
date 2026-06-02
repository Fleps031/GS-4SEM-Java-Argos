package com.argos.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TB_LEITURA_SENSOR")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeituraSensor {

    @Id
    @SequenceGenerator(name = "LEITURA_SEQ", sequenceName = "SEQ_LEITURA_SENSOR", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LEITURA_SEQ")
    @Column(name = "ID_LEITURA")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ID_MISSAO", nullable = false)
    private Missao missao;

    @Enumerated(EnumType.STRING)
    @Column(name = "TP_SENSOR", nullable = false)
    private TipoSensor tipoSensor;

    @Column(name = "VL_LEITURA", nullable = false)
    private Double valorLido;

    @Column(name = "DS_UNIDADE", nullable = false, length = 20)
    private String unidade;

    @Column(name = "DT_LEITURA", nullable = false)
    private LocalDateTime dataLeitura;

    @Column(name = "FL_ANOMALIA", nullable = false)
    private Boolean anomalia = false;

    @PrePersist
    protected void onCreate() {
        if (this.dataLeitura == null) {
            this.dataLeitura = LocalDateTime.now();
        }
        if (this.anomalia == null) {
            this.anomalia = false;
        }
    }

}
