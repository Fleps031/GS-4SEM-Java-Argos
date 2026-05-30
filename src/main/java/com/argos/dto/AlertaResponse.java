package com.argos.dto;

import com.argos.model.Severidade;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter @Builder
public class AlertaResponse {
    private Long id;
    private Long missaoId;
    private String mensagem;
    private Severidade severidade;
    private Boolean resolvido;
    private LocalDateTime dataAlerta;
}