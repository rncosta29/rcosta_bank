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
    void shouldReturnEmptyListWhenNoBills() {
        // Arrange
        when(creditCardBillsRepository.findAll()).thenReturn(List.of());

        // Act
        List<CreditCardBillsDto> result = creditCardBillsService.allBills();

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
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
    
    @Test
    void shouldGetBillsByCreditCardId() {
        // Arrange
        CreditCardModel creditCardModel = new CreditCardModel(1L, "Test Credit Card");
        CreditCardBillsModel bill1 = new CreditCardBillsModel(1L, "Test Bill 1", LocalDate.now(), 1, 2025, 100.0, false, creditCardModel);
        CreditCardBillsModel bill2 = new CreditCardBillsModel(2L, "Test Bill 2", LocalDate.now(), 2, 2025, 150.0, false, creditCardModel);
        CreditCardBillsDto billDto1 = new CreditCardBillsDto();
        CreditCardBillsDto billDto2 = new CreditCardBillsDto();

        when(creditCardBillsRepository.findByCreditCardId(1L)).thenReturn(List.of(bill1, bill2));
        when(modelMapper.map(bill1, CreditCardBillsDto.class)).thenReturn(billDto1);
        when(modelMapper.map(bill2, CreditCardBillsDto.class)).thenReturn(billDto2);

        // Act
        List<CreditCardBillsDto> result = creditCardBillsService.getBillsByCreditCardId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getPaymentMonth()); // Verifica a ordenação
        assertEquals(2, result.get(1).getPaymentMonth()); // Verifica a ordenação
        verify(creditCardBillsRepository, times(1)).findByCreditCardId(1L);
    }

    @Test
    void shouldAddNewBillsWithParcel() {
        // Arrange
        CreditCardBillsDto billDto = new CreditCardBillsDto();
        billDto.setCreditCardId(1L);
        billDto.setPrice(300.0); // Valor total
        billDto.setIsParcel(true);
        billDto.setPaymentMonth(1);
        billDto.setPaymentYear(2025);
        billDto.setName("Test Bill");

        CreditCardModel creditCardModel = new CreditCardModel(1L, "Test Credit Card");
        when(creditCardRepository.findById(1L)).thenReturn(Optional.of(creditCardModel));

        // Act
        List<CreditCardBillsDto> result = creditCardBillsService.addNewBills(billDto, 3); // Criar 3 parcelas

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size()); // Verificar se 3 parcelas foram criadas
        assertEquals(100.0, result.get(0).getPrice()); // Verificar o valor da parcela
        assertEquals(100.0, result.get(1).getPrice()); // Verificar o valor da parcela
        assertEquals(100.0, result.get(2).getPrice()); // Verificar o valor da parcela
    }
    
    @Test
    void shouldMapEntityToDtoCorrectlyWhenAddingBill() {
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
        verify(modelMapper, times(1)).map(billModel, CreditCardBillsDto.class);
    }
    
    @Test
    void shouldMapEntityToDtoCorrectlyWhenGettingBillById() {
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
        verify(modelMapper, times(1)).map(billModel, CreditCardBillsDto.class);
    }

    @Test
    void shouldReturnEmptyListWhenNoBillsForCreditCard() {
        // Arrange
        when(creditCardBillsRepository.findByCreditCardId(1L)).thenReturn(List.of());

        // Act
        List<CreditCardBillsDto> result = creditCardBillsService.getBillsByCreditCardId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(creditCardBillsRepository, times(1)).findByCreditCardId(1L);
    }

    @Test
    void shouldReturnSortedBillsByPaymentYear() {
        // Arrange
        CreditCardBillsModel bill1 = new CreditCardBillsModel(1L, "Bill 1", LocalDate.now(), 5, 2025, 100.0, false, new CreditCardModel());
        CreditCardBillsModel bill2 = new CreditCardBillsModel(2L, "Bill 2", LocalDate.now(), 10, 2025, 100.0, false, new CreditCardModel());
        when(creditCardBillsRepository.findByCreditCardId(1L)).thenReturn(List.of(bill2, bill1));
        
        CreditCardBillsDto billDto1 = new CreditCardBillsDto();
        CreditCardBillsDto billDto2 = new CreditCardBillsDto();
        when(modelMapper.map(bill1, CreditCardBillsDto.class)).thenReturn(billDto1);
        when(modelMapper.map(bill2, CreditCardBillsDto.class)).thenReturn(billDto2);

        // Act
        List<CreditCardBillsDto> result = creditCardBillsService.getBillsByCreditCardId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(billDto1, result.get(0)); // Verifica que billDto1 é o primeiro devido à ordenação
        assertEquals(billDto2, result.get(1)); // Verifica que billDto2 é o segundo
        verify(creditCardBillsRepository, times(1)).findByCreditCardId(1L);
    }

    @Test
    void shouldAddNonParcelBill() {
        // Arrange
        CreditCardBillsDto billDto = new CreditCardBillsDto();
        billDto.setCreditCardId(1L);
        billDto.setIsParcel(false);
        
        CreditCardModel creditCardModel = new CreditCardModel(1L, "Test Credit Card");
        CreditCardBillsModel billModel = new CreditCardBillsModel(1L, "Test Bill", LocalDate.now(), 1, 2025, 100.0, false, creditCardModel);
        
        when(creditCardRepository.findById(1L)).thenReturn(Optional.of(creditCardModel));
        when(modelMapper.map(billDto, CreditCardBillsModel.class)).thenReturn(billModel);
        when(modelMapper.map(billModel, CreditCardBillsDto.class)).thenReturn(billDto);

        // Act
        List<CreditCardBillsDto> result = creditCardBillsService.addNewBills(billDto, 1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(creditCardBillsRepository, times(1)).save(billModel);
    }

    @Test
    void shouldAddParcelBills() {
        // Arrange
        CreditCardBillsDto billDto = new CreditCardBillsDto();
        billDto.setCreditCardId(1L);
        billDto.setIsParcel(true);
        billDto.setPaymentMonth(12);
        billDto.setPaymentYear(2025);
        
        CreditCardModel creditCardModel = new CreditCardModel(1L, "Test Credit Card");
        CreditCardBillsModel billModel = new CreditCardBillsModel(1L, "Test Bill", LocalDate.now(), 12, 2025, 100.0, true, creditCardModel);
        
        when(creditCardRepository.findById(1L)).thenReturn(Optional.of(creditCardModel));
        when(modelMapper.map(billDto, CreditCardBillsModel.class)).thenReturn(billModel);
        when(modelMapper.map(billModel, CreditCardBillsDto.class)).thenReturn(billDto);

        // Act
        List<CreditCardBillsDto> result = creditCardBillsService.addNewBills(billDto, 3);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size()); // Verifica que 3 parcelas foram criadas
        verify(creditCardBillsRepository, times(3)).save(billModel);
    }

    @Test
    void shouldNotDeleteBillIfNotFound() {
        // Arrange
        when(creditCardBillsRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> creditCardBillsService.deleteBillById(1L));
    }
}
