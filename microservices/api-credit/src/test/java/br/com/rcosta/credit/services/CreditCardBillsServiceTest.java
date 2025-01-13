package br.com.rcosta.credit.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import br.com.rcosta.credit.dto.CreditCardBillsDto;
import br.com.rcosta.credit.models.CreditCardBillsModel;
import br.com.rcosta.credit.models.CreditCardModel;
import br.com.rcosta.credit.repositories.CreditCardBillsRepository;
import br.com.rcosta.credit.repositories.CreditCardRepository;
import jakarta.persistence.EntityNotFoundException;

public class CreditCardBillsServiceTest {

    @Mock
    private CreditCardBillsRepository creditCardBillsRepository;

    @Mock
    private CreditCardRepository creditCardRepository;

    @Mock
    private ModelMapper modelMapper;

    private CreditCardBillsService creditCardBillsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        creditCardBillsService = new CreditCardBillsService(creditCardBillsRepository, creditCardRepository, modelMapper);
    }

    @Test
    void shouldReturnAllBills() {
        // Arrange
        CreditCardBillsModel bill = new CreditCardBillsModel(1L, "Test Bill", LocalDate.now(), 1, 2025, 100.0, false, new CreditCardModel());
        CreditCardBillsDto billDto = new CreditCardBillsDto();
        when(creditCardBillsRepository.findAll()).thenReturn(List.of(bill));
        when(modelMapper.map(bill, CreditCardBillsDto.class)).thenReturn(billDto);

        // Act
        List<CreditCardBillsDto> result = creditCardBillsService.allBills();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(creditCardBillsRepository, times(1)).findAll();
    }

    @Test
    void shouldAddBill() {
        // Arrange
        CreditCardBillsDto billDto = new CreditCardBillsDto();
        billDto.setCreditCardId(1L);

        CreditCardModel creditCardModel = new CreditCardModel(1L, "Test Credit Card");
        CreditCardBillsModel billModel = new CreditCardBillsModel(1L, "Test Bill", LocalDate.now(), 1, 2025, 100.0, false, creditCardModel);

        when(creditCardRepository.findById(1L)).thenReturn(Optional.of(creditCardModel));
        when(modelMapper.map(billDto, CreditCardBillsModel.class)).thenReturn(billModel);
        when(modelMapper.map(billModel, CreditCardBillsDto.class)).thenReturn(billDto);

        // Act
        CreditCardBillsDto result = creditCardBillsService.addBills(billDto);

        // Assert
        assertNotNull(result);
        assertEquals(billDto.getCreditCardId(), result.getCreditCardId());
        verify(creditCardBillsRepository, times(1)).save(billModel);
    }

    @Test
    void shouldThrowExceptionWhenAddingBillWithInvalidCreditCard() {
        // Arrange
        CreditCardBillsDto billDto = new CreditCardBillsDto();
        billDto.setCreditCardId(1L);

        when(creditCardRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> creditCardBillsService.addBills(billDto));
    }

    @Test
    void shouldDeleteBill() {
        // Arrange
        CreditCardBillsModel billModel = new CreditCardBillsModel(1L, "Test Bill", LocalDate.now(), 1, 2025, 100.0, false, new CreditCardModel());
        when(creditCardBillsRepository.findById(1L)).thenReturn(Optional.of(billModel));

        // Act
        creditCardBillsService.deleteBillById(1L);

        // Assert
        verify(creditCardBillsRepository, times(1)).delete(billModel);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonexistentBill() {
        // Arrange
        when(creditCardBillsRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> creditCardBillsService.deleteBillById(1L));
    }

    @Test
    void shouldGetBillById() {
        // Arrange
        CreditCardBillsModel billModel = new CreditCardBillsModel(1L, "Test Bill", LocalDate.now(), 1, 2025, 100.0, false, new CreditCardModel());
        CreditCardBillsDto billDto = new CreditCardBillsDto();
        when(creditCardBillsRepository.findById(1L)).thenReturn(Optional.of(billModel));
        when(modelMapper.map(billModel, CreditCardBillsDto.class)).thenReturn(billDto);

        // Act
        CreditCardBillsDto result = creditCardBillsService.getBillsById(1L);

        // Assert
        assertNotNull(result);
        verify(creditCardBillsRepository, times(1)).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenGettingNonexistentBillById() {
        // Arrange
        when(creditCardBillsRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> creditCardBillsService.getBillsById(1L));
    }
}
