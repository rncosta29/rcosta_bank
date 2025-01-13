package br.com.rcosta.credit.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import br.com.rcosta.credit.dto.CreditCardDto;
import br.com.rcosta.credit.models.CreditCardModel;
import br.com.rcosta.credit.repositories.CreditCardBillsRepository;
import br.com.rcosta.credit.repositories.CreditCardRepository;
import jakarta.persistence.EntityNotFoundException;

public class CreditCardServiceTest {

    @Mock
    private CreditCardRepository creditCardRepository;

    @Mock
    private CreditCardBillsRepository creditCardBillsRepository;

    @Mock
    private ModelMapper modelMapper;

    private CreditCardService creditCardService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        creditCardService = new CreditCardService(creditCardRepository, creditCardBillsRepository, modelMapper);
    }

    @Test
    void shouldReturnAllCreditCards() {
        // Arrange
        CreditCardModel card1 = new CreditCardModel(1L, "Visa");
        CreditCardModel card2 = new CreditCardModel(2L, "Mastercard");
        CreditCardDto cardDto1 = new CreditCardDto();
        cardDto1.setId(1L);
        cardDto1.setName("Visa");
        CreditCardDto cardDto2 = new CreditCardDto();
        cardDto2.setId(2L);
        cardDto2.setName("Mastercard");

        when(creditCardRepository.findAll()).thenReturn(Arrays.asList(card1, card2));
        when(modelMapper.map(card1, CreditCardDto.class)).thenReturn(cardDto1);
        when(modelMapper.map(card2, CreditCardDto.class)).thenReturn(cardDto2);
        when(creditCardBillsRepository.findByCreditCardId(1L)).thenReturn(List.of());
        when(creditCardBillsRepository.findByCreditCardId(2L)).thenReturn(List.of());

        // Act
        List<CreditCardDto> result = creditCardService.allCreditsCard();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Visa", result.get(0).getName());
        assertEquals("Mastercard", result.get(1).getName());
        verify(creditCardRepository, times(1)).findAll();
    }

    @Test
    void shouldAddCreditCard() {
        // Arrange
        CreditCardDto cardDto = new CreditCardDto();
        cardDto.setName("Visa");
        CreditCardModel cardModel = new CreditCardModel();
        cardModel.setName("Visa");

        when(modelMapper.map(cardDto, CreditCardModel.class)).thenReturn(cardModel);
        when(creditCardRepository.save(cardModel)).thenReturn(cardModel);
        when(modelMapper.map(cardModel, CreditCardDto.class)).thenReturn(cardDto);

        // Act
        CreditCardDto result = creditCardService.addCreditCard(cardDto);

        // Assert
        assertNotNull(result);
        assertEquals("Visa", result.getName());
        verify(creditCardRepository, times(1)).save(cardModel);
    }

    @Test
    void shouldReturnCreditCardById() {
        // Arrange
        CreditCardModel cardModel = new CreditCardModel(1L, "Visa");
        CreditCardDto cardDto = new CreditCardDto();
        cardDto.setId(1L);
        cardDto.setName("Visa");

        when(creditCardRepository.findById(1L)).thenReturn(Optional.of(cardModel));
        when(modelMapper.map(cardModel, CreditCardDto.class)).thenReturn(cardDto);
        when(creditCardBillsRepository.findByCreditCardId(1L)).thenReturn(List.of());

        // Act
        CreditCardDto result = creditCardService.getCreditsCardById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Visa", result.getName());
        verify(creditCardRepository, times(1)).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenCreditCardNotFound() {
        // Arrange
        when(creditCardRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> creditCardService.getCreditsCardById(1L));
        verify(creditCardRepository, times(1)).findById(1L);
    }
}
