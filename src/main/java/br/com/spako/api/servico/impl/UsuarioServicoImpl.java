package br.com.spako.api.servico.impl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import br.com.spako.api.entidades.Usuario;
import br.com.spako.api.repositorio.UsuarioRepositorio;
import br.com.spako.api.servico.UsuarioServico;

@Service
public class UsuarioServicoImpl implements UsuarioServico{

	private static final Logger log = LoggerFactory.getLogger(UsuarioServicoImpl.class);
	
	@Autowired
	private UsuarioRepositorio usuarioRepositorio;
	
	@Cacheable("usuarios")
	public List<Usuario> buscaTodos() {
		log.info("Buscando todos usuários");
		return usuarioRepositorio.findAll(); 
	}

	@Cacheable("usuarios")
	public Optional<Usuario> buscaPorId(long id) {
		log.info("Buscando usuario por id {}", id);
		return usuarioRepositorio.findById(id);
	}

	@Cacheable("usuarios")
	public Optional<Usuario> buscaPorEmail(String email) {
		log.info("Buscando usuario por email {}", email);
		return usuarioRepositorio.findByEmail(email);
	}
	
	@Cacheable("usuarios")
	public Optional<Usuario> buscaPorCpf(String cpf) {
		log.info("Buscando usuário por cpf {}", cpf);
		return usuarioRepositorio.findByCpf(cpf);
	}

	@CachePut("usuarios")
	public Usuario persistir(Usuario u) {
		log.info("Salvando usuario {}", u.getNome());
		return usuarioRepositorio.save(u);
	}
	
	public void remover(long id) {
		log.info("removendo usuário com id{}", id);
		usuarioRepositorio.deleteById(id);
	}

}
