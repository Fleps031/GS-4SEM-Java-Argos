package com.argos.controller;

import com.argos.dto.*;
import com.argos.model.StatusMissao;
import com.argos.service.MissaoService;
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
@RequestMapping("/api/missoes")
@RequiredArgsConstructor
@Tag(name = "Missões", description = "Gerenciamento de missões espaciais")
public class MissaoController {

    private final MissaoService missaoService;

    @PostMapping
    @Operation(summary = "Criar missão", description = "Cadastra uma nova missão. Status inicial sempre PLANEJADA e risco BAIXO.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Missão criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<MissaoResponse> criar(@Valid @RequestBody CriarMissaoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(missaoService.criar(request));
    }

    @GetMapping
    @Operation(summary = "Listar missões", description = "Lista todas as missões. Filtro opcional por status: ?status=ATIVA")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    public ResponseEntity<List<MissaoResponse>> listar(
            @RequestParam(required = false) StatusMissao status) {
        return ResponseEntity.ok(missaoService.listarTodos(status));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar missão por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Missão encontrada"),
        @ApiResponse(responseCode = "404", description = "Missão não encontrada")
    })
    public ResponseEntity<MissaoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(missaoService.buscarPorId(id));
    }

    @GetMapping("/area/{area}")
    @Operation(summary = "Buscar missões por área de operação")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    public ResponseEntity<List<MissaoResponse>> buscarPorArea(@PathVariable String area) {
        return ResponseEntity.ok(missaoService.buscarPorArea(area));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar missão", description = "Atualiza nome, descrição e área. Não permite alterar missão CANCELADA.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Missão atualizada"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Missão não encontrada"),
        @ApiResponse(responseCode = "409", description = "Operação não permitida para o status atual")
    })
    public ResponseEntity<MissaoResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody AtualizarMissaoRequest request) {
        return ResponseEntity.ok(missaoService.atualizar(id, request));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Alterar status da missão", description = "Segue a máquina de estados: PLANEJADA → ATIVA → CONCLUIDA / CANCELADA")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Status atualizado"),
        @ApiResponse(responseCode = "400", description = "Transição de status inválida"),
        @ApiResponse(responseCode = "404", description = "Missão não encontrada"),
        @ApiResponse(responseCode = "409", description = "Operação não permitida")
    })
    public ResponseEntity<MissaoResponse> atualizarStatus(
            @PathVariable Long id,
            @Valid @RequestBody AtualizarStatusRequest request) {
        return ResponseEntity.ok(missaoService.atualizarStatus(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir missão", description = "Não permite excluir missão com status ATIVA.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Missão excluída"),
        @ApiResponse(responseCode = "404", description = "Missão não encontrada"),
        @ApiResponse(responseCode = "409", description = "Não é possível excluir missão ATIVA")
    })
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        missaoService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/resumo")
    @Operation(summary = "Dashboard resumo", description = "Retorna total de missões, agrupamentos por status e área, e missões de risco ALTO ou CRÍTICO.")
    @ApiResponse(responseCode = "200", description = "Resumo gerado com sucesso")
    public ResponseEntity<ResumoDashboardResponse> resumo() {
        return ResponseEntity.ok(missaoService.gerarResumo());
    }
}