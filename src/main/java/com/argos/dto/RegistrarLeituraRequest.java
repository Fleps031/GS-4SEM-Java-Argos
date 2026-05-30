package com.argos.dto;

import com.argos.model.TipoSensor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RegistrarLeituraRequest {

    @NotNull(message = "O ID da missão é obrigatório")
    private Long missaoId;

    @NotNull(message = "O tipo do sensor é obrigatório")
    private TipoSensor tipoSensor;

    @NotNull(message = "O valor lido é obrigatório")
    private Double valorLido;

    @NotBlank(message = "A unidade é obrigatória")
    @Size(max = 20, message = "A unidade deve ter no máximo 20 caracteres")
    private String unidade;
}