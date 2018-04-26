package br.com.spako.api.controle;

import java.security.NoSuchAlgorithmException;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.spako.api.dtos.UsuarioDto;
import br.com.spako.api.entidades.Usuario;
import br.com.spako.api.response.Response;
import br.com.spako.api.servico.UsuarioServico;

@RestController
@RequestMapping("/api/usuario")
@CrossOrigin(origins = "*")
public class UsuarioControle {
	
	private static final Logger log = LoggerFactory.getLogger(Usuario.class);
	
	@Autowired
	private UsuarioServico servico;

	public UsuarioControle() {
	}
	
	@PostMapping
	public ResponseEntity<Response<UsuarioDto>> cadastrar(@Valid @RequestBody UsuarioDto dto,
			BindingResult result) throws NoSuchAlgorithmException {
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
	
	/**
	 * Método que verifica se o usuário já existe
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
	
	/** Converte dados do dto para um Usuario
	 * @param dto
	 * @return
	 */
	private Usuario converterDtoEntidade(UsuarioDto dto) {
		Usuario u = new Usuario();
		u.setCpf(dto.getCpf());
		u.setEmail(dto.getEmail());
		u.setNome(dto.getNome());
		u.setPrimeiroLogin(dto.isPrimeiroLogin());
		u.setSenha(dto.getSenha());
		u.setSobrenome(dto.getSobrenome());
		u.setTipo(dto.getTipo());

		return u;
	}
	
	/**
	 * Converte dados de um Usuario para DTO
	 * @param u
	 * @return
	 */
	private UsuarioDto converterEntidadeDto(Usuario u) {
		UsuarioDto dto= new UsuarioDto();
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
}
