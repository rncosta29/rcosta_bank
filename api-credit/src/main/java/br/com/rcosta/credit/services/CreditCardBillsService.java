package br.com.rcosta.credit.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import br.com.rcosta.credit.dto.CreditCardBillsDto;
import br.com.rcosta.credit.dto.CreditCardDto;
import br.com.rcosta.credit.models.CreditCardBillsModel;
import br.com.rcosta.credit.models.CreditCardModel;
import br.com.rcosta.credit.repositories.CreditCardBillsRepository;
import br.com.rcosta.credit.repositories.CreditCardRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class CreditCardBillsService {

	private CreditCardBillsRepository creditCardBillsRepository;
	private CreditCardRepository creditCardRepository;
	private ModelMapper modelMapper;
	
	public CreditCardBillsService(CreditCardBillsRepository creditCardBillsRepository, CreditCardRepository creditCardRepository, ModelMapper modelMapper) {
		this.creditCardBillsRepository = creditCardBillsRepository;
		this.creditCardRepository = creditCardRepository;
		this.modelMapper = modelMapper;
	}
	
	public List<CreditCardBillsDto> allBills() {
		return creditCardBillsRepository.findAll().stream().map(c -> modelMapper.map(c, CreditCardBillsDto.class))
				.collect(Collectors.toList());
	}
	
	public CreditCardBillsDto addBills(CreditCardBillsDto dto) {
		CreditCardModel creditCardModel = creditCardRepository.findById(dto.getCreditCardId())
				.orElseThrow(() -> new EntityNotFoundException());
		
		dto.setCreditCardDto(modelMapper.map(creditCardModel, CreditCardDto.class));
		CreditCardBillsModel model = modelMapper.map(dto, CreditCardBillsModel.class);
		
		model.setCreditCard(creditCardModel);
		creditCardBillsRepository.save(model);
		
		return modelMapper.map(model, CreditCardBillsDto.class);
	}
	
	public CreditCardBillsDto getBillsById(Long id) {
		CreditCardBillsModel model =  creditCardBillsRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException());
		
		return modelMapper.map(model, CreditCardBillsDto.class);
	}
	
	public List<CreditCardBillsDto> getBillsByCreditCardId(Long creditCardId) {
	    return creditCardBillsRepository.findByCreditCardId(creditCardId).stream()
	            .sorted(Comparator.comparing(CreditCardBillsModel::getPaymentYear))  // Ordena pelo paymentYear em ordem crescente
	            .map(c -> modelMapper.map(c, CreditCardBillsDto.class))
	            .collect(Collectors.toList());
	}
	
	public List<CreditCardBillsDto> addNewBills(CreditCardBillsDto model, Integer quantity) {
	    List<CreditCardBillsDto> savedBills = new ArrayList<>();  // Lista para armazenar os DTOs das parcelas

	    if (model.getIsParcel()) {
	        int paymentMonth = model.getPaymentMonth();
	        int paymentYear = model.getPaymentYear();
	        
	        // Garanta que o CreditCardModel esteja gerenciado
	        CreditCardModel creditCardModel = creditCardRepository.findById(model.getCreditCardId())
	                                                               .orElseThrow(() -> new RuntimeException("Cartão de crédito não encontrado"));

	        // Cria as parcelas
	        for (int i = 0; i < quantity; i++) {
	            if (paymentMonth > 12) {
	                paymentMonth = 1;
	                paymentYear++;
	            }

	            // Cria a nova parcela
	            CreditCardBillsDto parcel = new CreditCardBillsDto();
	            parcel.setName(model.getName());
	            parcel.setPrice(model.getPrice());  // Divide o valor total pela quantidade de parcelas
	            parcel.setIsParcel(true);
	            parcel.setPaymentMonth(paymentMonth);
	            parcel.setPaymentYear(paymentYear);
	            parcel.setCreditCardId(model.getCreditCardId());
	            parcel.setCreditCardDto(model.getCreditCardDto());

	            // Usa a data recebida no modelo (não define uma data nova)
	            parcel.setDate(model.getDate());  // Configura a data vinda da requisição

	            // Mapeia o dto para o modelo e associa o CreditCardModel gerenciado
	            CreditCardBillsModel entity = modelMapper.map(parcel, CreditCardBillsModel.class);
	            entity.setCreditCard(creditCardModel); // Garantir que o CreditCardModel esteja associado

	            // Salva a parcela
	            creditCardBillsRepository.save(entity);

	            // Adiciona o DTO da parcela salva à lista
	            CreditCardBillsDto savedParcelDto = modelMapper.map(entity, CreditCardBillsDto.class);
	            savedBills.add(savedParcelDto);

	            // Incrementa o mês para a próxima parcela
	            paymentMonth++;
	        }
	    } else {
	        // Se não for parcelado, salva a fatura normalmente
	        CreditCardModel creditCardModel = creditCardRepository.findById(model.getCreditCardId())
	                                                               .orElseThrow(() -> new RuntimeException("Cartão de crédito não encontrado"));
	        CreditCardBillsModel entity = modelMapper.map(model, CreditCardBillsModel.class);
	        entity.setCreditCard(creditCardModel);

	        // Usa a data recebida no modelo (não define uma data nova)
	        entity.setDate(model.getDate());  // Configura a data vinda da requisição

	        creditCardBillsRepository.save(entity);

	        // Adiciona o DTO da fatura não parcelada à lista
	        CreditCardBillsDto savedBillDto = modelMapper.map(entity, CreditCardBillsDto.class);
	        savedBills.add(savedBillDto);
	    }

	    return savedBills;  // Retorna a lista com todos os DTOs das parcelas salvas
	}
	
	public void deleteBillById(Long id) {
	    // Verifica se a fatura existe antes de deletar
	    CreditCardBillsModel bill = creditCardBillsRepository.findById(id)
	        .orElseThrow(() -> new EntityNotFoundException("Fatura com id " + id + " não encontrada."));

	    // Realiza a exclusão
	    creditCardBillsRepository.delete(bill);
	}
}
