package org.sahabat.pulau;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.thymeleaf.dialect.springdata.SpringDataDialect;

@EnableAsync
@SpringBootApplication
public class SahabatPulauApplication {

	public static void main(String[] args) {
		SpringApplication.run(SahabatPulauApplication.class, args);
	}

	@Bean
	public SpringDataDialect springDataDialect() {

		return new SpringDataDialect();
	}


}
