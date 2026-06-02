package com.argos.controller;

import com.argos.dto.AutenticacaoResponse;
import com.argos.dto.LoginRequest;
import com.argos.dto.RegistroRequest;
import com.argos.service.AutenticacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Registro de usuários e login")
public class AutenticacaoController {

    private final AutenticacaoService autenticacaoService;

    @PostMapping("/registro")
    @Operation(summary = "Registrar novo usuário", description = "Cria uma nova conta com email e senha")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuário registrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Email já existe")
    })
    public ResponseEntity<AutenticacaoResponse> registrar(
            @Valid @RequestBody RegistroRequest request) {
        AutenticacaoResponse response = autenticacaoService.registrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Autentica usuário e retorna token JWT")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login bem-sucedido"),
            @ApiResponse(responseCode = "400", description = "Email ou senha inválidos"),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
    })
    public ResponseEntity<AutenticacaoResponse> login(
            @Valid @RequestBody LoginRequest request) {
        AutenticacaoResponse response = autenticacaoService.login(request);
        return ResponseEntity.ok(response);
    }
}
