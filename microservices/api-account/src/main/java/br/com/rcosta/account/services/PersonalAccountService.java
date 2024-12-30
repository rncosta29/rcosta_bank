package br.com.rcosta.account.services;

import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import br.com.rcosta.account.dto.DebitAccountDto;
import br.com.rcosta.account.dto.PersonalAccountDto;
import br.com.rcosta.account.models.DebitAccountModel;
import br.com.rcosta.account.models.PersonalAccountModel;
import br.com.rcosta.account.repositories.DebitAccountRepository;
import br.com.rcosta.account.repositories.PersonalAccountRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class PersonalAccountService {

	private PersonalAccountRepository personalAccountRepository;
	private DebitAccountRepository debitAccountRepository;
	private ModelMapper modelMapper;
	
	public PersonalAccountService(PersonalAccountRepository personalAccountRepository, DebitAccountRepository debitAccountRepository, ModelMapper modelMapper) {
		this.personalAccountRepository = personalAccountRepository;
		this.debitAccountRepository = debitAccountRepository;
		this.modelMapper = modelMapper;
	}
	
	public PersonalAccountDto addPersonalAccount(PersonalAccountDto dto) {
        PersonalAccountModel model = modelMapper.map(dto, PersonalAccountModel.class);
        personalAccountRepository.save(model);
        return modelMapper.map(model, PersonalAccountDto.class);
    }

    public PersonalAccountDto getPersonalAccountById(Long id) {
        PersonalAccountModel model = personalAccountRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada."));
        model.updateBalance();
        return modelMapper.map(model, PersonalAccountDto.class);
    }

    @Transactional
	public DebitAccountDto addDebitToPersonalAccount(Long personalAccountId, DebitAccountDto debitDto) {
	    PersonalAccountModel personalAccount = personalAccountRepository.findById(personalAccountId)
	        .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada."));

	    Hibernate.initialize(personalAccount.getDebitAccounts());

	    DebitAccountModel debit = modelMapper.map(debitDto, DebitAccountModel.class);
	    debit.setPersonalAccount(personalAccount);

	    debitAccountRepository.save(debit);
	    debitAccountRepository.flush(); // Sincroniza imediatamente com o banco

	    personalAccount.updateBalance();
	    personalAccountRepository.save(personalAccount);
	    personalAccountRepository.flush(); // Sincroniza o saldo atualizado

	    return modelMapper.map(debit, DebitAccountDto.class);
	}
}
