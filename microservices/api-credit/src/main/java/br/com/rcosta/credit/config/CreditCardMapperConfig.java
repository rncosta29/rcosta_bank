package br.com.rcosta.credit.config;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.rcosta.credit.dto.CreditCardDto;
import br.com.rcosta.credit.models.CreditCardModel;

@Configuration
public class CreditCardMapperConfig {

	@Bean
	@Qualifier("creditCardModelMapper")
	public ModelMapper creditCardModelMapper() {
		ModelMapper modelMapper = new ModelMapper();

		// Mapeamento entre CreditCardModel e CreditCardDto
		modelMapper.createTypeMap(CreditCardModel.class, CreditCardDto.class).addMappings(mapper -> {
			// Adicionar lógica específica se necessário
		});

		// Mapeamento entre CreditCardDto e CreditCardModel
		modelMapper.createTypeMap(CreditCardDto.class, CreditCardModel.class).addMappings(mapper -> {
			// Adicionar lógica específica se necessário
		});

		return modelMapper;
	}
}
