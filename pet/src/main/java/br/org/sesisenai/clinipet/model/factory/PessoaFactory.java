package br.org.sesisenai.clinipet.model.factory;

import br.org.sesisenai.clinipet.model.entity.Atendente;
import br.org.sesisenai.clinipet.model.entity.Cliente;
import br.org.sesisenai.clinipet.model.entity.Pessoa;
import br.org.sesisenai.clinipet.model.entity.Veterinario;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class PessoaFactory {
    public Pessoa getPessoa(SimpleGrantedAuthority authority, Pessoa pessoa) {
        switch (authority.getAuthority()) {
            case "Atendente" -> {
                Atendente atendente = new Atendente();
                atendente.setId(pessoa.getId());
                atendente.setNome(pessoa.getNome());
                atendente.setEmail(pessoa.getEmail());
                atendente.setSenha(pessoa.getSenha());
                atendente.setTelefone(pessoa.getTelefone());
                atendente.setSenha(pessoa.getSenha());

                return atendente;
            }

            case "Cliente" -> {
                Cliente cliente = new Cliente();
                cliente.setId(pessoa.getId());
                cliente.setNome(pessoa.getNome());
                cliente.setEmail(pessoa.getEmail());
                cliente.setSenha(pessoa.getSenha());
                cliente.setTelefone(pessoa.getTelefone());
                cliente.setSenha(pessoa.getSenha());
                cliente.setAnimais(((Cliente) pessoa).getAnimais());

                return cliente;
            }

            case "Veterinario" -> {
                Veterinario veterinario = new Veterinario();
                veterinario.setId(pessoa.getId());
                veterinario.setNome(pessoa.getNome());
                veterinario.setEmail(pessoa.getEmail());
                veterinario.setSenha(pessoa.getSenha());
                veterinario.setTelefone(pessoa.getTelefone());
                veterinario.setSenha(pessoa.getSenha());
                veterinario.setEspecialidade(((Veterinario) pessoa).getEspecialidade());

                return veterinario;
            }
        }

        throw new IllegalArgumentException("Pessoa não encontrada");
    }
}