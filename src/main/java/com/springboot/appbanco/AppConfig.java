package com.springboot.appbanco;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration

public class AppConfig {
	
	@Value("${config.base.endpoint}")
	private String url;
	
	
	@Value("${config.base.endpoint.persoau}")
	private String urlPersoAutho;
	
	@Value("${config.base.endpoint.accountdue}")
	private String urlAccountDue;

	@Bean
	@Qualifier("client")
	public WebClient registrarWebClient() {

		return WebClient.create(url); //EndPoint para conectarse con ese MS.
	}
	
	
	@Bean
	@Qualifier("personAutho")
	public WebClient registrarWebClientPA() {

		return WebClient.create(urlPersoAutho); //EndPoint para conectarse con ese MS.
	}
	
	@Bean
	@Qualifier("accountDue")
	public WebClient registrarWebClientAD() {

		return WebClient.create(urlAccountDue); //EndPoint para conectarse con ese MS.
	}
	
}
