package br.org.sesisenai.clinipet.security.utils;

import br.org.sesisenai.clinipet.security.model.entity.UserJpa;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtUtils {
    private final String senhaForte = "c127a7b6adb013a5ff879ae71afa62afa4b4ceb72afaa54711dbcde67b6dc325";

    public String gerarToken(UserJpa userJpa) {
        return Jwts.builder()
                .setIssuer("Clinipet")
                .setSubject(userJpa.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + 1800000))
                .signWith(SignatureAlgorithm.HS256, senhaForte)
                .compact();
    }

    public void validarToken(String token) {
        try {
            Jwts.parser().setSigningKey(senhaForte).parseClaimsJws(token);
            System.out.println("Token validado");
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(senhaForte).parseClaimsJws(token).getBody().getSubject();
    }
}