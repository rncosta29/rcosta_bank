package br.com.rcosta.credit.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.rcosta.credit.dto.CreditCardBillsDto;
import br.com.rcosta.credit.models.CreditCardBillsModel;
import br.com.rcosta.credit.models.CreditCardModel;
import br.com.rcosta.credit.repositories.CreditCardBillsRepository;
import br.com.rcosta.credit.repositories.CreditCardRepository;
import jakarta.persistence.EntityNotFoundException;

@SpringBootTest
public class CreditCardBillsServiceTest {

	@InjectMocks
    private CreditCardBillsService creditCardBillsService;

    @Mock
    private CreditCardBillsRepository creditCardBillsRepository;

    @Mock
    private CreditCardRepository creditCardRepository;

    @Mock
    private ModelMapper modelMapper;

    private CreditCardBillsDto creditCardBillsDto;
    private CreditCardModel creditCardModel;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        creditCardBillsDto = new CreditCardBillsDto();
        creditCardBillsDto.setId(1L);
        creditCardBillsDto.setName("Test Bill");
        creditCardBillsDto.setPrice(100.0);
        creditCardBillsDto.setIsParcel(false);
        creditCardBillsDto.setPaymentMonth(1);
        creditCardBillsDto.setPaymentYear(2025);
        creditCardBillsDto.setCreditCardId(1L);

        creditCardModel = new CreditCardModel();
        creditCardModel.setId(1L);
        creditCardModel.setName("1234567890123456");
    }

    @Test
    void addBills_ShouldReturnAddedBill() {
        when(creditCardRepository.findById(1L)).thenReturn(java.util.Optional.of(creditCardModel));
        when(modelMapper.map(creditCardBillsDto, CreditCardBillsModel.class)).thenReturn(new CreditCardBillsModel());
        when(modelMapper.map(any(CreditCardBillsModel.class), eq(CreditCardBillsDto.class))).thenReturn(creditCardBillsDto);
        
        CreditCardBillsDto result = creditCardBillsService.addBills(creditCardBillsDto);

        assertNotNull(result);
        assertEquals(creditCardBillsDto.getId(), result.getId());
        verify(creditCardBillsRepository, times(1)).save(any(CreditCardBillsModel.class));
    }

    @Test
    void addBills_ShouldThrowEntityNotFoundException_WhenCreditCardNotFound() {
        when(creditCardRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            creditCardBillsService.addBills(creditCardBillsDto);
        });
    }

    @Test
    void deleteBillById_ShouldDeleteBill_WhenBillExists() {
        when(creditCardBillsRepository.findById(1L)).thenReturn(java.util.Optional.of(new CreditCardBillsModel()));

        assertDoesNotThrow(() -> creditCardBillsService.deleteBillById(1L));
        verify(creditCardBillsRepository, times(1)).delete(any(CreditCardBillsModel.class));
    }

    @Test
    void deleteBillById_ShouldThrowEntityNotFoundException_WhenBillDoesNotExist() {
        when(creditCardBillsRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> creditCardBillsService.deleteBillById(1L));
    }
}
