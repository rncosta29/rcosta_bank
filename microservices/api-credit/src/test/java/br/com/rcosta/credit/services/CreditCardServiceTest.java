package br.com.rcosta.credit.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import br.com.rcosta.credit.dto.CreditCardDto;
import br.com.rcosta.credit.models.CreditCardModel;
import br.com.rcosta.credit.repositories.CreditCardRepository;

@ExtendWith(MockitoExtension.class)
public class CreditCardServiceTest {

    @InjectMocks
    private CreditCardService creditCardService;

    @Mock
    private CreditCardRepository creditCardRepository;

    @Mock
    private ModelMapper modelMapper;

    @Test
    void testAllCreditsCard() {
        // Configurando mocks
        CreditCardModel model1 = new CreditCardModel(1L, "Visa");
        CreditCardModel model2 = new CreditCardModel(2L, "Mastercard");

        when(creditCardRepository.findAll()).thenReturn(Arrays.asList(model1, model2));

        CreditCardDto dto1 = new CreditCardDto();
        dto1.setId(model1.getId());
        dto1.setName(model1.getName());

        CreditCardDto dto2 = new CreditCardDto();
        dto2.setId(model2.getId());
        dto2.setName(model2.getName());

        when(modelMapper.map(model1, CreditCardDto.class)).thenReturn(dto1);
        when(modelMapper.map(model2, CreditCardDto.class)).thenReturn(dto2);

        // Chamando o método de teste
        List<CreditCardDto> result = creditCardService.allCreditsCard();

        // Verificações
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Visa", result.get(0).getName());
        assertEquals("Mastercard", result.get(1).getName());
    }
}
