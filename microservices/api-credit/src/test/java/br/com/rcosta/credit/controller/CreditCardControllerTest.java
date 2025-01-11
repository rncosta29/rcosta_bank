package br.com.rcosta.credit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import br.com.rcosta.credit.dto.CreditCardDto;
import br.com.rcosta.credit.services.CreditCardService;

public class CreditCardControllerTest {

	@InjectMocks
    private CreditCardController creditCardController;

    @Mock
    private CreditCardService creditCardService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(creditCardController).build();
    }

    @Test
    void testFindAllCreditCard() throws Exception {
        CreditCardDto dto1 = new CreditCardDto();
        dto1.setId(1L);
        dto1.setName("Visa");

        CreditCardDto dto2 = new CreditCardDto();
        dto2.setId(2L);
        dto2.setName("Mastercard");

        when(creditCardService.allCreditsCard()).thenReturn(Arrays.asList(dto1, dto2));

        mockMvc.perform(get("/api/v1/credit-card")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Visa"))
                .andExpect(jsonPath("$[1].name").value("Mastercard"));
    }

    @Test
    void testCreateCreditCard() throws Exception {
        CreditCardDto dto = new CreditCardDto();
        dto.setId(1L);
        dto.setName("Visa");

        when(creditCardService.addCreditCard(any())).thenReturn(dto);

        mockMvc.perform(post("/api/v1/credit-card/insert")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Visa\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Visa"));
    }

    @Test
    void testFindCreditCardById() throws Exception {
        CreditCardDto dto = new CreditCardDto();
        dto.setId(1L);
        dto.setName("Visa");

        when(creditCardService.getCreditsCardById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/v1/credit-card/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Visa"));
    }
}
