package com.argos.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Projeto Argos - API de Manutenção Preditiva Espacial",
                version = "1.0.0",
                description = "API REST para monitoramento de missões espaciais, registro de leituras de sensores e gerenciamento de alertas operacionais.",
                contact = @Contact(name = "Grupo 2ESPR")
        )
)
public class OpenApiConfig {
}
