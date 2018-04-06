package br.com.spako.api.servico.impl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.spako.api.entidades.Usuario;
import br.com.spako.api.repositorio.UsuarioRepositorio;
import br.com.spako.api.servico.UsuarioServico;

@Service
public class UsuarioServicoImpl implements UsuarioServico{

	private static final Logger log = LoggerFactory.getLogger(UsuarioServicoImpl.class);
	
	@Autowired
	private UsuarioRepositorio usuarioRepositorio;
	
	@Override
	public List<Usuario> buscaTodos() {
		log.info("Buscando todos usuários");
		return usuarioRepositorio.findAll(); 
	}

	@Override
	public Optional<Usuario> buscaPorId(long id) {
		log.info("Buscando usuario por id {}", id);
		return usuarioRepositorio.findById(id);
	}

	@Override
	public Usuario buscaPorEmail(String email) {
		log.info("Buscando usuario por email {}", email);
		return usuarioRepositorio.findByEmail(email);
	}

//	@Override
//	public Optional<Usuario> autenticarUsuario(String email) {
//		log.info("Autenticar usuario {}", email);
//		return usuarioRepositorio.
//	}
//
//	@Override
//	public Usuario persistir(Usuario u) {
//		log.info("Salvando usuario {}", u.getNome());
//		
//	}
//
//	@Override
//	public Usuario update(Usuario u) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Usuario delete(long id) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public List<Usuario> buscaPorNome(String nome) {
//		// TODO Auto-generated method stub
//		return null;
//	}

}
