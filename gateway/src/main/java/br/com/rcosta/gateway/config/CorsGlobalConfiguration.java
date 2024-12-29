package br.com.rcosta.gateway.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsGlobalConfiguration implements WebMvcConfigurer {

	public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Aplica CORS a todos os endpoints
                .allowedOriginPatterns("*") // Permite todas as origens
                .allowedMethods("*")       // Permite todos os métodos HTTP
                .allowedHeaders("*")       // Permite todos os cabeçalhos
                .allowCredentials(true);   // Permite credenciais (se necessário)
    }
}
