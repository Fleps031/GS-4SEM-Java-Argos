package com.argos.dto;

import jakarta.validation.constraints.*;

public class CriarMissaoRequest {

    @NotBlank(message = "Nome da missão é obrigatório")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    private String nome;

    @NotBlank(message = "Descrição é obrigatória")
    @Size(min = 5, max = 500, message = "Descrição deve ter entre 5 e 500 caracteres")
    private String descricao;

    @NotBlank(message = "Área de operação é obrigatória")
    @Size(min = 3, max = 150, message = "Área de operação deve ter entre 3 e 150 caracteres")
    private String areaOperacao;

    public CriarMissaoRequest() {
    }

    public CriarMissaoRequest(String nome, String descricao, String areaOperacao) {
        this.nome = nome;
        this.descricao = descricao;
        this.areaOperacao = areaOperacao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getAreaOperacao() {
        return areaOperacao;
    }

    public void setAreaOperacao(String areaOperacao) {
        this.areaOperacao = areaOperacao;
    }
}
