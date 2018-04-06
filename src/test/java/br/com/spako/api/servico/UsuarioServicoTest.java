package br.com.spako.api.servico;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.spako.api.entidades.Usuario;
import br.com.spako.api.repositorio.UsuarioRepositorio;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class UsuarioServicoTest {
	
	private static final String EMAIL = "teste@teste.com";
	
	@MockBean
	private UsuarioRepositorio usuarioRepositorio;
	
	@Autowired
	private UsuarioServico usuarioServico;

	@Before
	public void setUp() throws Exception {
		BDDMockito.given(this.usuarioRepositorio.findByEmail(Mockito.anyString())).willReturn(new Usuario());
		BDDMockito.given(this.usuarioRepositorio.save(Mockito.any(Usuario.class))).willReturn(new Usuario());
	}
	
	@Test
	public void testBuscaPorEmail() {
		Usuario u = usuarioServico.buscaPorEmail(EMAIL);
		
		assertNotNull(u);
	}
}
