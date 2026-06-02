package com.argos.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret:minha-chave-secreta-super-segura-para-o-projeto-argos-2024-2025}")
    private String jwtSecret;

    @Value("${jwt.expiration:86400000}")
    private long jwtExpirationMs;

    private SecretKey getSigningKey() {
        try {
            // Gera uma chave de 512 bits (64 bytes) usando SHA-256 da chave configurada
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(jwtSecret.getBytes(StandardCharsets.UTF_8));
            // Usa os 64 bytes do hash para gerar uma chave de 512 bits
            byte[] keyBytes = new byte[64];
            System.arraycopy(hash, 0, keyBytes, 0, 32);
            System.arraycopy(hash, 0, keyBytes, 32, 32);
            return Keys.hmacShaKeyFor(keyBytes);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar chave JWT", e);
        }
    }

    public String gerarToken(Long usuarioId, String email) {
        return Jwts.builder()
                .subject(email)
                .claim("usuarioId", usuarioId)
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public String extrairEmail(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public Long extrairUsuarioId(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("usuarioId", Long.class);
    }

    public boolean validarToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("Token expirado", e);
        } catch (UnsupportedJwtException e) {
            throw new RuntimeException("Token não suportado", e);
        } catch (MalformedJwtException e) {
            throw new RuntimeException("Token inválido", e);
        } catch (SignatureException e) {
            throw new RuntimeException("Assinatura do token inválida", e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("String do token vazia", e);
        }
    }
}
