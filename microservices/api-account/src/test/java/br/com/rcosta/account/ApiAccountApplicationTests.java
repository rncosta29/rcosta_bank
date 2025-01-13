package br.com.rcosta.account;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class ApiAccountApplicationTests {

	@Test
	void contextLoads() {
		// Este teste garante que o contexto do Spring seja carregado corretamente.
	}

}
