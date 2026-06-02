package com.argos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AutenticacaoResponse {

    private String token;
    private String tipo;
    private Long id;
    private String email;
    private String nome;

    public static AutenticacaoResponse of(String token, Long id, String email, String nome) {
        return AutenticacaoResponse.builder()
                .token(token)
                .tipo("Bearer")
                .id(id)
                .email(email)
                .nome(nome)
                .build();
    }
}
