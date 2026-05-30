package com.argos.dto;

import jakarta.validation.constraints.NotNull;
import com.argos.model.StatusMissao;

public class AtualizarStatusRequest {

    @NotNull(message = "Status da missão é obrigatório")
    private StatusMissao status;

    public AtualizarStatusRequest() {
    }

    public AtualizarStatusRequest(StatusMissao status) {
        this.status = status;
    }

    public StatusMissao getStatus() {
        return status;
    }

    public void setStatus(StatusMissao status) {
        this.status = status;
    }
}
