package br.com.spako.api.servico;

import java.util.List;
import java.util.Optional;

import br.com.spako.api.entidades.Usuario;

public interface UsuarioServico {
	
	List<Usuario> buscaTodos();

    Optional<Usuario> buscaPorId(long id);
    
    Optional<Usuario> buscaPorEmail(String email);
    
    Optional<Usuario> buscaPorCpf(String cpf);
        
    Usuario persistir(Usuario u);

    void remover(long id);
}
