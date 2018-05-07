package br.com.spako.api.controle;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.spako.api.dtos.UsuarioDto;
import br.com.spako.api.entidades.Usuario;
import br.com.spako.api.servico.UsuarioServico;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UsuarioControleTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private UsuarioServico servico;

	private static final String URL = "/api/usuario/";
	private static final String EMAIL = "teste@email.com";
	private static final Long ID = 1L;
	private static final String NOME = "Teste";
	private static final String SOBRENOME = "Testador";
	private static final String CPF = "391.455.098-83";
	private static final String SENHA = "$2a$08$US/nGf3a0KwzcO16CgLxle0ZsJxeOF5tAyCthA8UqdVW/qWznoPPa";
	private static final int TIPO = 2;

	@Test
	@WithMockUser
	public void testBuscarPorEmailInexistente() throws Exception {
		BDDMockito.given(servico.buscaPorEmail(Mockito.anyString())).willReturn(Optional.empty());

		mvc.perform(MockMvcRequestBuilders.get(URL + "?email=" + EMAIL).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors").value("Usuário não encontrado para email " + EMAIL));
	}

	@Test
	@WithMockUser
	public void testBucarPorEmailExistente() throws Exception {
		BDDMockito.given(servico.buscaPorEmail(Mockito.anyString())).willReturn(Optional.of(this.obterUsuario()));

		mvc.perform(MockMvcRequestBuilders.get(URL + "?email=" + EMAIL).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data[0].id").value(ID))
				.andExpect(jsonPath("$.data[0].nome").value(NOME))
				.andExpect(jsonPath("$.data[0].sobrenome").value(SOBRENOME))
				.andExpect(jsonPath("$.data[0].cpf").value(CPF))
				.andExpect(jsonPath("$.data[0].email").value(EMAIL))
				.andExpect(jsonPath("$.data[0].senha").value(SENHA))
				.andExpect(jsonPath("$.data[0].primeiroLogin").value(false))
				.andExpect(jsonPath("$.data[0].tipo").value(TIPO))
				.andExpect(jsonPath("$.errors").isEmpty());
	}

	@Test
	@WithMockUser
	public void testBuscaTodos() throws Exception {
		BDDMockito.given(servico.buscaTodos()).willReturn((this.obterUsuarioLista()));

		mvc.perform(MockMvcRequestBuilders.get(URL).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data[0].id").value(ID))
				.andExpect(jsonPath("$.data[0].nome").value(NOME))
				.andExpect(jsonPath("$.data[0].sobrenome").value(SOBRENOME))
				.andExpect(jsonPath("$.data[0].cpf").value(CPF))
				.andExpect(jsonPath("$.data[0].email").value(EMAIL))
				.andExpect(jsonPath("$.data[0].senha").value(SENHA))
				.andExpect(jsonPath("$.data[0].primeiroLogin").value(false))
				.andExpect(jsonPath("$.data[0].tipo").value(TIPO))
				.andExpect(jsonPath("$.errors").isEmpty());
	}

	@Test
	@WithMockUser
	public void testCadastrar() throws Exception {
		Usuario usuario = obterUsuario();
		BDDMockito.given(servico.persistir(Mockito.any(Usuario.class))).willReturn(usuario);

		mvc.perform(MockMvcRequestBuilders.post(URL)
				.content(obterConteudoPost())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data.nome").value(NOME))
				.andExpect(jsonPath("$.data.sobrenome").value(SOBRENOME))
				.andExpect(jsonPath("$.data.cpf").value(CPF))
				.andExpect(jsonPath("$.data.email").value(EMAIL))
				.andExpect(jsonPath("$.data.senha").value(SENHA))
				.andExpect(jsonPath("$.data.primeiroLogin").value(false))
				.andExpect(jsonPath("$.data.tipo").value(TIPO))
				.andExpect(jsonPath("$.errors").isEmpty());
	}

	@Test
	@WithMockUser
	public void testRemover() throws Exception {
		BDDMockito.given(servico.buscaPorId(Mockito.anyLong())).willReturn(Optional.of(new Usuario()));
		
		mvc.perform(MockMvcRequestBuilders.delete(URL + ID).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

	}

	private Usuario obterUsuario() {
		Usuario u = new Usuario();
		u.setId(ID);
		u.setNome(NOME);
		u.setSobrenome(SOBRENOME);
		u.setCpf(CPF);
		u.setEmail(EMAIL);
		u.setSenha(SENHA);
		u.setPrimeiroLogin(false);
		u.setTipo(TIPO);

		return u;
	}

	private List<Usuario> obterUsuarioLista() {
		Usuario u = new Usuario();
		u.setId(ID);
		u.setNome(NOME);
		u.setSobrenome(SOBRENOME);
		u.setCpf(CPF);
		u.setEmail(EMAIL);
		u.setSenha(SENHA);
		u.setPrimeiroLogin(false);
		u.setTipo(TIPO);

		List<Usuario> lista = new ArrayList<>();
		lista.add(u);
		return lista;
	}

	private String obterConteudoPost() throws JsonProcessingException {
		UsuarioDto dto = new UsuarioDto();
		dto.setNome(NOME);
		dto.setSobrenome(SOBRENOME);
		dto.setCpf(CPF);
		dto.setEmail(EMAIL);
		dto.setSenha(SENHA);
		dto.setPrimeiroLogin(false);
		dto.setTipo(TIPO);

		ObjectMapper obj = new ObjectMapper();
		return obj.writeValueAsString(dto);
	}
}
