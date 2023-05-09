package br.org.sesisenai.clinipet.security.model.entity;

import br.org.sesisenai.clinipet.model.entity.Pessoa;
//import br.org.sesisenai.clinipet.security.utils.UserJpaDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
//@JsonDeserialize(using = UserJpaDeserializer.class)
public class UserJpa implements UserDetails {
    private Pessoa pessoa;
    private List<Perfil> authorities;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;
    private String username;
    private String password;

    public UserJpa(Pessoa pessoa) {
        this.pessoa = pessoa;
        this.accountNonExpired = true;
        this.accountNonLocked = true;
        this.credentialsNonExpired = true;
        this.enabled = true;
        this.password = pessoa.getSenha();
        this.username = pessoa.getEmail();
        this.authorities = new ArrayList<>();
        this.authorities.add(Perfil.perfilOf(pessoa.getClass().getSimpleName()));
    }
}
