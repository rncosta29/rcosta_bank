package br.com.rcosta.account.services;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import br.com.rcosta.account.dto.DebitAccountDto;
import br.com.rcosta.account.models.DebitAccountModel;
import br.com.rcosta.account.models.PersonalAccountModel;
import br.com.rcosta.account.repositories.DebitAccountRepository;
import br.com.rcosta.account.repositories.PersonalAccountRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class DebitAccountService {

	private DebitAccountRepository debitAccountRepository;
	private PersonalAccountRepository personalAccountRepository;
	private ModelMapper modelMapper;
	
	public DebitAccountService(DebitAccountRepository debitAccountRepository, PersonalAccountRepository personalAccountRepository, ModelMapper modelMapper) {
		this.debitAccountRepository = debitAccountRepository;
		this.personalAccountRepository = personalAccountRepository;
		this.modelMapper = modelMapper;
	}
	
	public List<DebitAccountDto> allDebits() {
		return debitAccountRepository.findAll().stream().map(c -> modelMapper.map(c, DebitAccountDto.class))
				.collect(Collectors.toList());
	}
	
	public DebitAccountDto addDebit(DebitAccountDto dto) {
        // Busca a conta pessoal associada ao débito
        PersonalAccountModel personalAccount = personalAccountRepository.findById(dto.getPersonalAccountId())
            .orElseThrow(() -> new EntityNotFoundException("Conta pessoal não encontrada."));

        // Mapeia o DTO para o modelo de débito
        DebitAccountModel debitAccount = modelMapper.map(dto, DebitAccountModel.class);
        debitAccount.setPersonalAccount(personalAccount);

        // Salva o débito e atualiza o saldo da conta pessoal
        debitAccountRepository.save(debitAccount);
        personalAccount.updateBalance();
        personalAccountRepository.save(personalAccount);

        return modelMapper.map(debitAccount, DebitAccountDto.class);
    }
	
	public DebitAccountDto getDebitById(Long id) {
		DebitAccountModel model = debitAccountRepository.findById(id)
			    .orElseThrow(EntityNotFoundException::new);
		
		return modelMapper.map(model, DebitAccountDto.class);
	}
	
	public List<DebitAccountDto> getDebitByPersonalAccountId(Long personalAccountId) {
	    return debitAccountRepository.findByPersonalAccountId(personalAccountId).stream()
	            .sorted(Comparator.comparing(DebitAccountModel::getDate))  // Ordena pelo paymentYear em ordem crescente
	            .map(c -> modelMapper.map(c, DebitAccountDto.class))
	            .collect(Collectors.toList());
	}
	
	public void deleteDebitById(Long id) {
	    // Busca o débito
	    DebitAccountModel debitAccount = debitAccountRepository.findById(id)
	        .orElseThrow(() -> new EntityNotFoundException("Débito com ID " + id + " não encontrado."));

	    // Obtém a conta pessoal associada
	    PersonalAccountModel personalAccount = debitAccount.getPersonalAccount();
	    if (personalAccount == null) {
	        throw new IllegalStateException("Débito não está associado a nenhuma conta pessoal.");
	    }

	    // Exclui o débito
	    debitAccountRepository.delete(debitAccount);

	    // Atualiza o saldo da conta pessoal
	    personalAccount.updateBalance();
	    personalAccountRepository.save(personalAccount);
	}
	
	public DebitAccountDto updateDebit(Long id, DebitAccountDto dto) {
	    // Busca o débito existente
	    DebitAccountModel debitAccount = debitAccountRepository.findById(id)
	        .orElseThrow(() -> new EntityNotFoundException("Débito com ID " + id + " não encontrado."));

	    // Atualiza os campos do débito
	    debitAccount.setDate(dto.getDate());
	    debitAccount.setInstitution(dto.getInstitution());
	    debitAccount.setPrice(dto.getPrice());

	    // Salva o débito atualizado
	    debitAccountRepository.save(debitAccount);

	    // Atualiza o saldo da conta pessoal associada
	    PersonalAccountModel personalAccount = debitAccount.getPersonalAccount();
	    if (personalAccount != null) {
	        personalAccount.updateBalance();
	        personalAccountRepository.save(personalAccount);
	    }

	    return modelMapper.map(debitAccount, DebitAccountDto.class);
	}
}
