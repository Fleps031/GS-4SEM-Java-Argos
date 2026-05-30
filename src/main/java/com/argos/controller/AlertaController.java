package com.argos.controller;

import com.argos.dto.AlertaResponse;
import com.argos.service.AlertaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alertas")
@RequiredArgsConstructor
@Tag(name = "Alertas", description = "Consulta e resolução de alertas operacionais das missões")
public class AlertaController {

    private final AlertaService alertaService;

    @GetMapping("/missao/{id}")
    @Operation(summary = "Listar alertas por missão", description = "Retorna todos os alertas de uma missão, ordenados do mais recente para o mais antigo.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Missão não encontrada")
    })
    public ResponseEntity<List<AlertaResponse>> listarPorMissao(@PathVariable Long id) {
        return ResponseEntity.ok(alertaService.listarPorMissao(id));
    }

    @GetMapping("/missao/{id}/pendentes")
    @Operation(summary = "Listar alertas pendentes", description = "Retorna apenas os alertas não resolvidos de uma missão.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Missão não encontrada")
    })
    public ResponseEntity<List<AlertaResponse>> listarPendentes(@PathVariable Long id) {
        return ResponseEntity.ok(alertaService.listarPendentesPorMissao(id));
    }

    @PatchMapping("/{id}/resolver")
    @Operation(
            summary = "Resolver alerta",
            description = "Marca um alerta como resolvido. Se o alerta for CRÍTICO, recalcula o nível de risco da missão automaticamente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Alerta resolvido com sucesso"),
            @ApiResponse(responseCode = "400", description = "Alerta já foi resolvido anteriormente"),
            @ApiResponse(responseCode = "404", description = "Alerta não encontrado")
    })
    public ResponseEntity<AlertaResponse> resolver(@PathVariable Long id) {
        return ResponseEntity.ok(alertaService.resolver(id));
    }

    @GetMapping("/criticos")
    @Operation(summary = "Listar alertas críticos", description = "Retorna todos os alertas com severidade CRÍTICO ainda não resolvidos de todas as missões.")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    public ResponseEntity<List<AlertaResponse>> listarCriticos() {
        return ResponseEntity.ok(alertaService.listarCriticos());
    }
}