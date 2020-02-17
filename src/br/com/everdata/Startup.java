package br.com.everdata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import br.com.everdata.services.ProofService;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class Startup {

	public static void main(String[] args) {
		ProofService.setLogin(args[0]);
		ProofService.setPassword(args[1]);
		SpringApplication.run(Startup.class, args);
	}

}
