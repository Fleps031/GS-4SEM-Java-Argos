package com.argos.service;

import com.argos.dto.AutenticacaoResponse;
import com.argos.dto.LoginRequest;
import com.argos.dto.RegistroRequest;
import com.argos.exception.CredenciaisInvalidasException;
import com.argos.exception.UsuarioJaExisteException;
import com.argos.model.Usuario;
import com.argos.repository.UsuarioRepository;
import com.argos.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AutenticacaoService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AutenticacaoResponse registrar(RegistroRequest request) {
        // Validar se email já existe
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new UsuarioJaExisteException("Email já registrado: " + request.getEmail());
        }

        // Criar novo usuário
        Usuario usuario = Usuario.builder()
                .email(request.getEmail())
                .nome(request.getNome())
                .senha(passwordEncoder.encode(request.getSenha()))
                .ativo(true)
                .build();

        usuarioRepository.save(usuario);

        // Gerar token
        String token = jwtUtil.gerarToken(usuario.getId(), usuario.getEmail());

        return AutenticacaoResponse.of(token, usuario.getId(), usuario.getEmail(), usuario.getNome());
    }

    public AutenticacaoResponse login(LoginRequest request) {
        // Buscar usuário por email
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CredenciaisInvalidasException("Email ou senha inválidos"));

        // Validar se usuário está ativo
        if (!usuario.getAtivo()) {
            throw new CredenciaisInvalidasException("Usuário inativo");
        }

        // Validar senha
        if (!passwordEncoder.matches(request.getSenha(), usuario.getSenha())) {
            throw new CredenciaisInvalidasException("Email ou senha inválidos");
        }

        // Gerar token
        String token = jwtUtil.gerarToken(usuario.getId(), usuario.getEmail());

        return AutenticacaoResponse.of(token, usuario.getId(), usuario.getEmail(), usuario.getNome());
    }
}
