package br.eti.freitas.startproject.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.expression.ParseException;

import br.eti.freitas.startproject.security.service.impl.DataBaseService;

@Configuration
@Profile("developer")
public class DeveloperEnvironmentConfig {

	@Autowired
	private DataBaseService dataBaseService;

	@Value("${spring.jpa.hibernate.ddl-auto}")
	private String strategy;

	@Bean
	public boolean iniciarBancoDeDados() throws ParseException {

		if (!"create".equals(strategy)) {
			return false;
		}

		dataBaseService.iniciarBancoTeste();
		return true;
	}

}
