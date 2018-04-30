package br.com.spako.api.repositorio;

import static org.junit.Assert.assertNotNull;

import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.spako.api.entidades.Usuario;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class UsuarioRepositorioTest {
	
	@Autowired
	private UsuarioRepositorio usuarioRepositorio;
	
	private static final String NOME = "Teste";
	private static final String EMAIL = "teste@teste.com";
	private static final long ID = 1;
	
	@Before
	public void setUp() throws Exception{
		Usuario u = new Usuario();
		u.setCpf("39145509883");
		u.setEmail(EMAIL);
		u.setNome(NOME);
		u.setPrimeiroLogin(false);
		u.setSobrenome("Sobrenome");
		u.setSenha("seha");
		u.setTipo(1);
		this.usuarioRepositorio.save(u);
	}
	
	@After
	public final void tearDown() {
		this.usuarioRepositorio.deleteAll();
	}
	
	@Test
	public void testBuscaPorEmail() {
		Optional<Usuario> u = this.usuarioRepositorio.findByEmail(EMAIL);
		
		assertNotNull(u.orElse(null));
	}

//	@Test
//	public void testBuscaPorNome() {
//		Usuario u = this.usuarioRepositorio.findByNome(NOME);
//		
//		assertEquals(NOME, u.getNome());
//	}
	
	@Test
	public void testBuscaPorId() {
		Optional<Usuario> u = this.usuarioRepositorio.findById(ID);
		
		assertNotNull(u.orElse(null));
	}
}
