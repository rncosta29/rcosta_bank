package br.com.rcosta.credit.config;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.rcosta.credit.dto.CreditCardBillsDto;
import br.com.rcosta.credit.models.CreditCardBillsModel;

@Configuration
public class CreditCardBillsMapperConfig {

	@Bean
	@Qualifier("creditCardBillsModelMapper")
	public ModelMapper creditCardBillsModelMapper() {
		ModelMapper modelMapper = new ModelMapper();

		// Mapeamento entre CreditCardBillsModel e CreditCardBillsDto
		modelMapper.createTypeMap(CreditCardBillsModel.class, CreditCardBillsDto.class).addMappings(mapper -> {
			mapper.map(CreditCardBillsModel::getCreditCard, CreditCardBillsDto::setCreditCardDto);
		});

		// Mapeamento entre CreditCardBillsDto e CreditCardBillsModel
		modelMapper.createTypeMap(CreditCardBillsDto.class, CreditCardBillsModel.class).addMappings(mapper -> {
			// Usando um conversor customizado para calcular e setar o preço
			mapper.using((org.modelmapper.Converter<CreditCardBillsDto, Double>) context -> {
				CreditCardBillsDto dto = context.getSource();
				// Supondo que o parcelamento será sempre 3
				return dto.getPrice() / 3; // Calculando preço dividido por 3
			}).map(CreditCardBillsDto::getPrice, CreditCardBillsModel::setPrice);
		});

		return modelMapper;
	}
}
