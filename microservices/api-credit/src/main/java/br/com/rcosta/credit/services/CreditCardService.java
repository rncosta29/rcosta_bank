package br.com.rcosta.credit.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import br.com.rcosta.credit.dto.CreditCardBillsDto;
import br.com.rcosta.credit.dto.CreditCardDto;
import br.com.rcosta.credit.models.CreditCardBillsModel;
import br.com.rcosta.credit.models.CreditCardModel;
import br.com.rcosta.credit.repositories.CreditCardBillsRepository;
import br.com.rcosta.credit.repositories.CreditCardRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class CreditCardService {

	private CreditCardRepository creditCardRepository;
	private CreditCardBillsRepository creditCardBillsRepository;
	private final ModelMapper creditCardModelMapper;
	
	public CreditCardService(CreditCardRepository creditCardRepository, CreditCardBillsRepository creditCardBillsRepository, @Qualifier("creditCardModelMapper") ModelMapper creditCardModelMapper) {
		this.creditCardBillsRepository = creditCardBillsRepository;
		this.creditCardRepository = creditCardRepository;
		this.creditCardModelMapper = creditCardModelMapper;
	}
	
	public List<CreditCardDto> allCreditsCard() {
		List<CreditCardDto> listModel = creditCardRepository.findAll().stream().map(c -> creditCardModelMapper.map(c, CreditCardDto.class))
				.collect(Collectors.toList());
		
		for (CreditCardDto creditCardDto : listModel) {
			List<CreditCardBillsModel> list = creditCardBillsRepository.findByCreditCardId(creditCardDto.getId());
			creditCardDto.setCreditsCardDto(list.stream().map(dto -> creditCardModelMapper.map(dto, CreditCardBillsDto.class)).collect(Collectors.toList()));
		}
		
		return listModel;
	}
	
	public CreditCardDto addCreditCard(CreditCardDto dto) {
		CreditCardModel model = creditCardModelMapper.map(dto, CreditCardModel.class);
		creditCardRepository.save(model);
		
		return creditCardModelMapper.map(model, CreditCardDto.class);
	}
	
	public CreditCardDto getCreditsCardById(Long id) {
		CreditCardModel model =  creditCardRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException());
		List<CreditCardBillsModel> list = creditCardBillsRepository.findByCreditCardId(id);
		CreditCardDto newModel = creditCardModelMapper.map(model, CreditCardDto.class);
		
		newModel.setCreditsCardDto(list.stream().map(dto -> creditCardModelMapper.map(dto, CreditCardBillsDto.class)).collect(Collectors.toList()));
		
		return newModel;
	}
}
