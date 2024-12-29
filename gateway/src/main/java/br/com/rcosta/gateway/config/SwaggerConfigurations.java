package br.com.rcosta.gateway.config;

import java.util.Objects;

import org.springdoc.core.properties.SwaggerUiConfigParameters;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfigurations {

	@Bean
	public CommandLineRunner openApiGroups(
	        RouteDefinitionLocator locator,
	        SwaggerUiConfigParameters swaggerUiParameters) {
	    return args -> Objects.requireNonNull(locator
	                    .getRouteDefinitions().collectList().block())
	            .stream()
	            .map(RouteDefinition::getId)
	            .filter(id -> id.startsWith("api-")) // Filtra qualquer microserviço que inicie com 'api-'
	            .forEach(swaggerUiParameters::addGroup); // Adiciona os grupos ao Swagger UI
	}
}
