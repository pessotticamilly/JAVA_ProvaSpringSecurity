package br.org.sesisenai.clinipet.security.controller;

import br.org.sesisenai.clinipet.security.model.dto.UsuarioDto;
import br.org.sesisenai.clinipet.security.model.entity.UserJpa;
import br.org.sesisenai.clinipet.security.utils.CookieUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.WebUtils;

@RestController
@RequestMapping("/login")
@CrossOrigin
@AllArgsConstructor
public class AutenticacaoController {
    private AuthenticationManager authenticationManager;
    private final CookieUtils cookieUtils = new CookieUtils();

    @PostMapping
    public ResponseEntity<Object> autenticacao (@RequestBody UsuarioDto usuarioDto, HttpServletResponse response) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(usuarioDto.getEmail(), usuarioDto.getSenha());
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            if(authentication.isAuthenticated()) {
                UserJpa userJpa = (UserJpa) authentication.getPrincipal();
                Cookie jwtCookie = cookieUtils.gerarTokenCookie(userJpa);

                response.addCookie(jwtCookie);

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userJpa.getPassword(), userJpa.getUsername(), userJpa.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

                return ResponseEntity.ok().build();
            }
        } catch (Exception e) {
            System.out.println("Erro de Autenticação (AutenticacaoController):");
            System.out.println(e);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Object> logout (HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = WebUtils.getCookie(request, "token");
        cookie.setPath("/");
        cookie.setMaxAge(0);

        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }
}