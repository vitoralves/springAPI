package br.com.spako.api.servico;

import java.util.List;
import java.util.Optional;

import br.com.spako.api.entidades.Usuario;

public interface UsuarioServico {
	
	List<Usuario> buscaTodos();

    Optional<Usuario> buscaPorId(long id);
    
    Optional<Usuario> buscaPorEmail(String email);
    
    Optional<Usuario> buscaPorCpf(String cpf);
    
//    Optional<Usuario> autenticarUsuario(String email);
//    
    Usuario persistir(Usuario u);
//
//    Usuario update(Usuario u);
//
//    Usuario delete(long id);
//
//    List<Usuario> buscaPorNome(String nome);
}
