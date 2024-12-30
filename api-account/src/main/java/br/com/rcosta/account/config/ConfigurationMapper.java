package br.com.rcosta.account.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigurationMapper {

    @Bean
    public ModelMapper obterModelMapper() {
        return new ModelMapper();
    }
}
