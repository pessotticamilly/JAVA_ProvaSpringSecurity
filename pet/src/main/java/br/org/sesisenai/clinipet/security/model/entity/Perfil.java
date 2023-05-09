package br.org.sesisenai.clinipet.security.model.entity;

import org.springframework.security.core.GrantedAuthority;

public enum Perfil implements GrantedAuthority {
    ATENDENTE("ATENDENTE"),
    CLIENTE("CLIENTE"),
    VETERINARIO("VETERINARIO");

    private String descricao;

    Perfil(String descricao) {
        this.descricao = descricao;
    }

    public static Perfil perfilOf(String simpleName) {
        return switch (simpleName) {
            case "Atendente" -> ATENDENTE;
            case "Cliente" -> CLIENTE;
            case "Veterinario" -> VETERINARIO;
            default -> null;
        };
    }

    @Override
    public String getAuthority() {
        return this.name();
    }
}
