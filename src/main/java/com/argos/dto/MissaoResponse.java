package com.argos.dto;

import com.argos.model.NivelRisco;
import com.argos.model.StatusMissao;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter @Builder
public class MissaoResponse {
    private Long id;
    private String nome;
    private String descricao;
    private StatusMissao status;
    private NivelRisco nivelRisco;
    private String areaOperacao;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
}