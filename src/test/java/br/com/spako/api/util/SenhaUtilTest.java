package br.com.spako.api.util;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class SenhaUtilTest {
	
	private static final String SENHA = "123456";
	private final BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
	
	@Test
	public void testSenhaNull() throws Exception {
		assertNull(SenhaUtil.gerarBcrypt(null));
	}
	
	@Test
	public void testGerarSenhaHash() throws Exception {
		String h = SenhaUtil.gerarBcrypt(SENHA);
		
		assertTrue(bcrypt.matches(SENHA, h));				
	}

}
