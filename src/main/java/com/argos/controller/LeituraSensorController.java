package com.argos.controller;

import com.argos.dto.LeituraSensorResponse;
import com.argos.dto.RegistrarLeituraRequest;
import com.argos.model.TipoSensor;
import com.argos.service.LeituraSensorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leituras")
@RequiredArgsConstructor
@Tag(name = "Leituras de Sensor", description = "Registro e consulta de leituras de sensores das missões")
public class LeituraSensorController {

    private final LeituraSensorService leituraService;

    @PostMapping
    @Operation(
            summary = "Registrar leitura",
            description = "Registra uma leitura de sensor. Avalia anomalia automaticamente, " +
                    "gera alerta se necessário e recalcula o nível de risco da missão.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Leitura registrada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou missão não está ATIVA"),
            @ApiResponse(responseCode = "404", description = "Missão não encontrada")
    })
    public ResponseEntity<LeituraSensorResponse> registrar(
            @Valid @RequestBody RegistrarLeituraRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(leituraService.registrar(request));
    }

    @GetMapping("/missao/{id}")
    @Operation(summary = "Listar leituras por missão", description = "Retorna todas as leituras de uma missão, ordenadas da mais recente para a mais antiga.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Missão não encontrada")
    })
    public ResponseEntity<List<LeituraSensorResponse>> listarPorMissao(@PathVariable Long id) {
        return ResponseEntity.ok(leituraService.listarPorMissao(id));
    }

    @GetMapping("/missao/{id}/anomalias")
    @Operation(summary = "Listar leituras anômalas", description = "Retorna apenas as leituras com anomalia detectada de uma missão.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Missão não encontrada")
    })
    public ResponseEntity<List<LeituraSensorResponse>> listarAnomalias(@PathVariable Long id) {
        return ResponseEntity.ok(leituraService.listarAnomaliasPorMissao(id));
    }

    @GetMapping("/missao/{id}/sensor/{tipo}")
    @Operation(summary = "Filtrar leituras por tipo de sensor", description = "Retorna leituras de um tipo específico de sensor para uma missão.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Missão não encontrada")
    })
    public ResponseEntity<List<LeituraSensorResponse>> listarPorSensor(
            @PathVariable Long id,
            @PathVariable TipoSensor tipo) {
        return ResponseEntity.ok(leituraService.listarPorMissaoETipoSensor(id, tipo));
    }
}