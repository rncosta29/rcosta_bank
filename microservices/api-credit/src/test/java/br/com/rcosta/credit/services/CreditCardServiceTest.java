package br.com.rcosta.credit.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.rcosta.credit.dto.CreditCardDto;
import br.com.rcosta.credit.models.CreditCardModel;
import br.com.rcosta.credit.repositories.CreditCardRepository;
import jakarta.persistence.EntityNotFoundException;

@SpringBootTest
public class CreditCardServiceTest {

	@InjectMocks
    private CreditCardService creditCardService;

    @Mock
    private CreditCardRepository creditCardRepository;

    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAllCreditsCard() {
        CreditCardModel model1 = new CreditCardModel(1L, "Visa");
        CreditCardModel model2 = new CreditCardModel(2L, "Mastercard");
        when(creditCardRepository.findAll()).thenReturn(Arrays.asList(model1, model2));

        CreditCardDto dto1 = new CreditCardDto();
        dto1.setId(1L);
        dto1.setName("Visa");
        CreditCardDto dto2 = new CreditCardDto();
        dto2.setId(2L);
        dto2.setName("Mastercard");

        when(modelMapper.map(model1, CreditCardDto.class)).thenReturn(dto1);
        when(modelMapper.map(model2, CreditCardDto.class)).thenReturn(dto2);

        List<CreditCardDto> result = creditCardService.allCreditsCard();

        assertEquals(2, result.size());
        assertEquals("Visa", result.get(0).getName());
        assertEquals("Mastercard", result.get(1).getName());
    }

    @Test
    void testAddCreditCard() {
        CreditCardDto dto = new CreditCardDto();
        dto.setName("Visa");

        CreditCardModel model = new CreditCardModel();
        model.setName("Visa");

        when(modelMapper.map(dto, CreditCardModel.class)).thenReturn(model);
        when(creditCardRepository.save(model)).thenReturn(model);
        when(modelMapper.map(model, CreditCardDto.class)).thenReturn(dto);

        CreditCardDto result = creditCardService.addCreditCard(dto);

        assertNotNull(result);
        assertEquals("Visa", result.getName());
    }

    @Test
    void testGetCreditsCardById() {
        CreditCardModel model = new CreditCardModel(1L, "Visa");
        when(creditCardRepository.findById(1L)).thenReturn(Optional.of(model));

        CreditCardDto dto = new CreditCardDto();
        dto.setId(1L);
        dto.setName("Visa");

        when(modelMapper.map(model, CreditCardDto.class)).thenReturn(dto);

        CreditCardDto result = creditCardService.getCreditsCardById(1L);

        assertNotNull(result);
        assertEquals("Visa", result.getName());
    }

    @Test
    void testGetCreditsCardById_NotFound() {
        when(creditCardRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            creditCardService.getCreditsCardById(1L);
        });
    }
}
