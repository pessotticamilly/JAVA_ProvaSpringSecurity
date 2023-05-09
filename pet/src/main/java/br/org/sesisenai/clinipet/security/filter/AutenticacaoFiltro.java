package br.org.sesisenai.clinipet.security.filter;

import br.org.sesisenai.clinipet.security.service.JpaService;
import br.org.sesisenai.clinipet.security.utils.CookieUtils;
import br.org.sesisenai.clinipet.security.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@AllArgsConstructor
public class AutenticacaoFiltro extends OncePerRequestFilter {
    private final CookieUtils cookieUtils = new CookieUtils();
    private final JwtUtils jwtUtils = new JwtUtils();

    private JpaService jpaService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = cookieUtils.getTokenCookie(request);
            jwtUtils.validarToken(token);

            UserDetails user = jpaService.loadUserByUsername(jwtUtils.getUsername(token));

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        } catch (Exception e) {
            System.out.println("Entrou no 1º catch");
//            e.printStackTrace();
            try {
                validarUrl(request.getRequestURI(), request.getMethod());
            } catch (Exception exception) {
                System.out.println("Entrou no 2º catch");
//                exception.printStackTrace();
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        } finally {
            System.out.println("Entrou no finally");
            filterChain.doFilter(request, response);
        }
    }

    private void validarUrl(String url, String metodo) throws RuntimeException {
        System.out.println("Entrou no validar url");
        System.out.println("URL: " + url + "\nMETODO: " + metodo);

        if (!(url.equals("/api/login") ||
                url.equals("/login") ||
                url.equals("/api/logout") ||
                url.equals("/logout") ||
                url.equals("/api/veterinario") /*&& metodo.equals("GET")*/||
                url.equals("/veterinario") /*&& metodo.equals("GET")*/ ||
                url.equals("/api/servico") /*&& metodo.equals("GET")*/ ||
                url.equals("/servico") /*&& metodo.equals("GET")*/)) {
            System.out.println("Entrou no validarUrl");
            throw new RuntimeException();
        }
    }
}