package com.argos.dto;

import com.argos.model.TipoSensor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter @Builder
public class LeituraSensorResponse {
    private Long id;
    private Long missaoId;
    private TipoSensor tipoSensor;
    private Double valorLido;
    private String unidade;
    private LocalDateTime dataLeitura;
    private Boolean anomalia;
}