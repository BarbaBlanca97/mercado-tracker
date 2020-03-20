package com.barbablanca.mercadotracker;

import com.barbablanca.mercadotracker.mailing.PendingVerificationsRepository;
import com.barbablanca.mercadotracker.mailing.MailSender;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.RedirectView;

import javax.sql.DataSource;

@EnableScheduling
@SpringBootApplication
@Controller
public class MercadoTrackerApplication {
	private MailSender mailSender;

	MercadoTrackerApplication (Environment environment) {
		mailSender = null;
	}

	public static void main(String[] args) {
		SpringApplication.run(MercadoTrackerApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	@Bean
	public MailSender mailSender(PendingVerificationsRepository pendingVerificationsRepository) {
		if( mailSender == null ) {
			mailSender = new MailSender(pendingVerificationsRepository);
		}

		return mailSender;
	}
}
