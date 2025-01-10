package br.com.rcosta.account.services;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import br.com.rcosta.account.dto.DebitAccountDto;
import br.com.rcosta.account.dto.PersonalAccountDto;
import br.com.rcosta.account.models.DebitAccountModel;
import br.com.rcosta.account.models.PersonalAccountModel;
import br.com.rcosta.account.repositories.DebitAccountRepository;
import br.com.rcosta.account.repositories.PersonalAccountRepository;
import jakarta.persistence.EntityNotFoundException;

public class PersonalAccountServiceTest {

	 @Mock
	    private PersonalAccountRepository personalAccountRepository;

	    @Mock
	    private DebitAccountRepository debitAccountRepository;

	    @Mock
	    private ModelMapper modelMapper;

	    private PersonalAccountService personalAccountService;

	    @BeforeEach
	    void setUp() {
	        MockitoAnnotations.openMocks(this);
	        personalAccountService = new PersonalAccountService(personalAccountRepository, debitAccountRepository, modelMapper);
	    }

	    @Test
	    void shouldAddPersonalAccount() {
	        // Arrange
	        PersonalAccountDto accountDto = new PersonalAccountDto();
	        PersonalAccountModel accountModel = new PersonalAccountModel(1L, "Account", 0.0);
	        when(modelMapper.map(accountDto, PersonalAccountModel.class)).thenReturn(accountModel);
	        when(personalAccountRepository.save(accountModel)).thenReturn(accountModel);
	        when(modelMapper.map(accountModel, PersonalAccountDto.class)).thenReturn(accountDto);

	        // Act
	        PersonalAccountDto result = personalAccountService.addPersonalAccount(accountDto);

	        // Assert
	        assertNotNull(result);
	        verify(personalAccountRepository, times(1)).save(accountModel);
	    }

	    @Test
	    void shouldThrowExceptionWhenPersonalAccountNotFound() {
	        // Arrange
	        when(personalAccountRepository.findById(1L)).thenReturn(Optional.empty());

	        // Act & Assert
	        assertThrows(EntityNotFoundException.class, () -> personalAccountService.getPersonalAccountById(1L));
	    }

	    @Test
	    void shouldAddDebitToPersonalAccount() {
	        DebitAccountDto debitDto = new DebitAccountDto();
	        PersonalAccountModel personalAccount = new PersonalAccountModel(1L, "Account", 0.0);
	        DebitAccountModel debitAccount = new DebitAccountModel(1L, "Institution", LocalDate.now(), 100.0);
	        when(personalAccountRepository.findById(1L)).thenReturn(Optional.of(personalAccount));
	        when(modelMapper.map(debitDto, DebitAccountModel.class)).thenReturn(debitAccount);
	        when(debitAccountRepository.save(debitAccount)).thenReturn(debitAccount);
	        when(modelMapper.map(debitAccount, DebitAccountDto.class)).thenReturn(debitDto);

	        // Act
	        DebitAccountDto result = personalAccountService.addDebitToPersonalAccount(1L, debitDto);

	        // Assert
	        assertNotNull(result);
	        verify(debitAccountRepository, times(1)).save(debitAccount);
	        verify(personalAccountRepository, times(1)).save(personalAccount);
	    }
}
