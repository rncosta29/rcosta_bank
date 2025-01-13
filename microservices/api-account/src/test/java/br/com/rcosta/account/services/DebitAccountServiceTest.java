package br.com.rcosta.account.services;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import br.com.rcosta.account.dto.DebitAccountDto;
import br.com.rcosta.account.models.DebitAccountModel;
import br.com.rcosta.account.models.PersonalAccountModel;
import br.com.rcosta.account.repositories.DebitAccountRepository;
import br.com.rcosta.account.repositories.PersonalAccountRepository;
import jakarta.persistence.EntityNotFoundException;

public class DebitAccountServiceTest {
	
	private static final String INSTITUTION_NAME = "Institution";
	private static final String ACCOUNT_NAME = "Account";

	@Mock
    private DebitAccountRepository debitAccountRepository;

    @Mock
    private PersonalAccountRepository personalAccountRepository;

    @Mock
    private ModelMapper modelMapper;

    private DebitAccountService debitAccountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        debitAccountService = new DebitAccountService(debitAccountRepository, personalAccountRepository, modelMapper);
    }

    @Test
    void shouldReturnAllDebits() {
        // Arrange
        DebitAccountModel debitAccount = new DebitAccountModel(1L, INSTITUTION_NAME, LocalDate.now(), 100.0);
        DebitAccountDto debitAccountDto = new DebitAccountDto();
        when(debitAccountRepository.findAll()).thenReturn(List.of(debitAccount));
        when(modelMapper.map(debitAccount, DebitAccountDto.class)).thenReturn(debitAccountDto);

        // Act
        List<DebitAccountDto> result = debitAccountService.allDebits();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(debitAccountRepository, times(1)).findAll();
    }

    @Test
    void shouldAddDebit() {
        // Arrange
        DebitAccountDto debitDto = new DebitAccountDto();
        debitDto.setPersonalAccountId(1L);
        PersonalAccountModel personalAccount = new PersonalAccountModel(1L, ACCOUNT_NAME, 0.0);
        DebitAccountModel debitAccount = new DebitAccountModel(1L, INSTITUTION_NAME, LocalDate.now(), 100.0);
        when(personalAccountRepository.findById(1L)).thenReturn(Optional.of(personalAccount));
        when(modelMapper.map(debitDto, DebitAccountModel.class)).thenReturn(debitAccount);
        when(modelMapper.map(debitAccount, DebitAccountDto.class)).thenReturn(debitDto);

        // Act
        DebitAccountDto result = debitAccountService.addDebit(debitDto);

        // Assert
        assertNotNull(result);
        assertEquals(debitDto.getPersonalAccountId(), result.getPersonalAccountId());
        verify(debitAccountRepository, times(1)).save(debitAccount);
        verify(personalAccountRepository, times(1)).save(personalAccount);
    }

    @Test
    void shouldThrowExceptionWhenDebitNotFound() {
        // Arrange
        when(debitAccountRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> debitAccountService.getDebitById(1L));
    }

    @Test
    void shouldDeleteDebit() {
        // Arrange
        DebitAccountModel debitAccount = new DebitAccountModel(1L, INSTITUTION_NAME, LocalDate.now(), 100.0);
        PersonalAccountModel personalAccount = new PersonalAccountModel(1L, ACCOUNT_NAME, 0.0);
        debitAccount.setPersonalAccount(personalAccount);

        when(debitAccountRepository.findById(1L)).thenReturn(Optional.of(debitAccount));

        // Act
        debitAccountService.deleteDebitById(1L);

        // Assert
        verify(debitAccountRepository, times(1)).delete(debitAccount);
        verify(personalAccountRepository, times(1)).save(personalAccount);
    }
}
