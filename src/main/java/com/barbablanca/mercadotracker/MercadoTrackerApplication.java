package com.barbablanca.mercadotracker;

import com.barbablanca.mercadotracker.mailing.PendingVerificationsRepository;
import com.barbablanca.mercadotracker.mailing.MailSender;
import com.barbablanca.mercadotracker.security.AuthorizationFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

@EnableScheduling
@SpringBootApplication
public class MercadoTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MercadoTrackerApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	@Bean
	public AuthorizationFilter authorizationFilter(Environment environment) {
		return new AuthorizationFilter(environment);
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
