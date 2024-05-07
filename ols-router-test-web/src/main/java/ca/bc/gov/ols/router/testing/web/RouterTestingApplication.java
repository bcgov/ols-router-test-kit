package ca.bc.gov.ols.router.testing.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.geolatte.geom.json.GeolatteGeomModule;
import com.fasterxml.jackson.databind.Module;

@SpringBootApplication
@ComponentScan({"ca.bc.gov.ols.router.testing.engine.dao", "ca.bc.gov.ols.router.testing.web"})
@EntityScan("ca.bc.gov.ols.router.testing.engine.entity")
@EnableJpaRepositories("ca.bc.gov.ols.router.testing.engine.dao")
@EnableAutoConfiguration
public class RouterTestingApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(RouterTestingApplication.class, args);
	}

	@Bean
    Module GeolatteModule() {
		return new GeolatteGeomModule();
    }
}
