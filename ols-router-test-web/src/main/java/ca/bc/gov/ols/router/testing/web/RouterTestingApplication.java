package ca.bc.gov.ols.router.testing.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class RouterTestingApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(RouterTestingApplication.class, args);
	}
}
