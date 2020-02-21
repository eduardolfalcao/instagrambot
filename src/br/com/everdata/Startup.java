package br.com.everdata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import br.com.everdata.instagram.utils.KeyUtils;
import br.com.everdata.services.ProofService;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class Startup {

	public static void main(String[] args) {		
		SpringApplication.run(Startup.class, args);
	}

}
