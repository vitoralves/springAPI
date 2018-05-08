package br.com.spako.api.controle;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.spako.api.dtos.UsuarioDto;
import br.com.spako.api.entidades.Usuario;
import br.com.spako.api.response.Response;
import br.com.spako.api.servico.UsuarioServico;
import br.com.spako.api.util.SenhaUtil;

@RestController
@RequestMapping("/api/usuario")
@CrossOrigin(origins = "*")
public class UsuarioControle {

	private static final Logger log = LoggerFactory.getLogger(Usuario.class);

	@Autowired
	private UsuarioServico servico;

	@Value("${paginacao.qtdPagina}")
	private int paginacao;

	public UsuarioControle() {
	}

	/**
	 * Faz buscas na entidade de acordo com os parâmetros passados na requisição
	 * 
	 * @param email
	 * @return
	 */
	@GetMapping
	public ResponseEntity<Response<List<UsuarioDto>>> obter(
			@RequestParam(value = "email", required = false) String email) {
		List<Usuario> lista = new ArrayList<>();
		Response<List<UsuarioDto>> response = new Response<List<UsuarioDto>>();

		if (email != null && !email.isEmpty()) {
			log.info("Buscando usuario por email: {}", email);
			Optional<Usuario> usuario = servico.buscaPorEmail(email);
			if (usuario.isPresent()) {
				lista.add(usuario.get());
			}
		} else {
			log.info("Buscando por todos usuários!");
			lista = servico.buscaTodos();
		}

		if (lista.isEmpty()) {
			log.info("Usuário não encontrado para email {}", email);
			response.getErrors().add("Usuário não encontrado para email " + email);
			return ResponseEntity.badRequest().body(response);
		}

		List<UsuarioDto> listDto = new ArrayList<>();
		lista.forEach(l -> listDto.add(this.converterEntidadeDto(l)));
		response.setData(listDto);
		return ResponseEntity.ok(response);
	}

	/*
	 * Insere novas entidades
	 */
	@PostMapping
	public ResponseEntity<Response<UsuarioDto>> cadastrar(@Valid @RequestBody UsuarioDto dto, BindingResult result)
			throws NoSuchAlgorithmException {
		log.info("Cadastrando usuário: {}", dto.toString());
		Response<UsuarioDto> response = new Response<UsuarioDto>();

		validarDadosExistentes(dto, result);
		Usuario usuario = this.converterDtoEntidade(dto);

		if (result.hasErrors()) {
			log.error("Erro validando dados usuario: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}

		this.servico.persistir(usuario);

		response.setData(converterEntidadeDto(usuario));
		return ResponseEntity.ok(response);
	}

	/*
	 * Atualiza uma entidade
	 */
	@PutMapping
	public ResponseEntity<Response<UsuarioDto>> atualizar(@Valid @RequestBody UsuarioDto dto, BindingResult result)
			throws NoSuchAlgorithmException {
		log.info("Atualizando usuário: {}", dto.toString());
		Response<UsuarioDto> response = new Response<UsuarioDto>();

		Optional<Usuario> usuario = servico.buscaPorId(dto.getId());
		if (!usuario.isPresent()) {
			result.addError(new ObjectError("Usuario", "Usuário não encontrado para o id " + dto.getId()));
		}

		autalizaUsuario(usuario.get(), dto, result);

		if (result.hasErrors()) {
			log.error("Erro validando dados usuario: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}

		this.servico.persistir(usuario.get());

		response.setData(converterEntidadeDto(usuario.get()));
		return ResponseEntity.ok(response);
	}

	@DeleteMapping(value="/{id}")
	public ResponseEntity<Response<String>> remover(@PathVariable("id") Long id) {
		log.info("Removendo usuário com id {}", id);
		Optional<Usuario> u = servico.buscaPorId(id);
		Response<String> response = new Response<String>();
		
		if (!u.isPresent()) {
			log.info("Usuario para id {} não encontrado", id);
			response.getErrors().add("Usuário não encontrado com id "+id);
			return ResponseEntity.badRequest().body(response);
		}
		
		servico.remover(id);
		return ResponseEntity.ok(new Response<String>());
	}

	/**
	 * Método que verifica se o usuário já existe
	 * 
	 * @param dto
	 * @param result
	 */
	private void validarDadosExistentes(UsuarioDto dto, BindingResult result) {
		this.servico.buscaPorEmail(dto.getEmail())
				.ifPresent(u -> result.addError(new ObjectError("usuario", "Usuário já existente para esse email.")));

		if (!dto.getCpf().isEmpty()) {
			this.servico.buscaPorCpf(dto.getCpf())
					.ifPresent(c -> result.addError(new ObjectError("usuario", "Usuário já cadastrado com esse cpf")));
		}

	}

	/**
	 * Converte dados do dto para um Usuario
	 * 
	 * @param dto
	 * @return
	 */
	private Usuario converterDtoEntidade(UsuarioDto dto) {
		Usuario u = new Usuario();
		u.setCpf(dto.getCpf());
		u.setEmail(dto.getEmail());
		u.setNome(dto.getNome());
		u.setPrimeiroLogin(dto.isPrimeiroLogin());
		u.setSenha(SenhaUtil.gerarBcrypt(dto.getSenha()));
		u.setSobrenome(dto.getSobrenome());
		u.setTipo(dto.getTipo());

		return u;
	}

	/**
	 * Converte dados de um Usuario para DTO
	 * 
	 * @param u
	 * @return
	 */
	private UsuarioDto converterEntidadeDto(Usuario u) {
		UsuarioDto dto = new UsuarioDto();
		dto.setId(u.getId());
		dto.setCpf(u.getCpf());
		dto.setEmail(u.getEmail());
		dto.setNome(u.getNome());
		dto.setPrimeiroLogin(u.isPrimeiroLogin());
		dto.setSenha(u.getSenha());
		dto.setSobrenome(u.getSobrenome());
		dto.setTipo(u.getTipo());

		return dto;
	}

	private void autalizaUsuario(Usuario u, UsuarioDto dto, BindingResult result) {
		u.setNome(dto.getNome());
		u.setSobrenome(dto.getSobrenome());
		u.setSenha(dto.getSenha());
		u.setPrimeiroLogin(dto.isPrimeiroLogin());
		u.setTipo(dto.getTipo());
		if (!u.getEmail().equals(dto.getEmail())) {
			servico.buscaPorEmail(dto.getEmail()).ifPresent(
					e -> result.addError(new ObjectError("usuario", "Email " + dto.getEmail() + " já cadastrado!")));
			u.setEmail(dto.getEmail());
		}
		if (!u.getCpf().equals(dto.getCpf())) {
			servico.buscaPorCpf(dto.getCpf()).ifPresent(
					c -> result.addError(new ObjectError("usuario", "CPF " + dto.getCpf() + " já cadastrado!")));
			u.setCpf(dto.getCpf());
		}
	}
}
