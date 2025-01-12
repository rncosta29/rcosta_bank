package br.com.rcosta.credit.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.rcosta.credit.dto.CreditCardBillsDto;
import br.com.rcosta.credit.services.CreditCardBillsService;
import jakarta.persistence.EntityNotFoundException;

public class CreditCardBillsControllerTest {

	@InjectMocks
    private CreditCardBillsController creditCardBillsController;

    @Mock
    private CreditCardBillsService creditCardBillsService;
    
    @Mock
    private UriComponentsBuilder uriBuilder;

    private CreditCardBillsDto creditCardBillsDto;

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
    }

    @Test
    void findAllBills_ShouldReturnListOfBills() {
        when(creditCardBillsService.allBills()).thenReturn(List.of(creditCardBillsDto));

        ResponseEntity<List<CreditCardBillsDto>> response = creditCardBillsController.findAllBills();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void createBills_ShouldReturnCreatedStatus_WhenBillsAreCreated() {
        // Mockando o comportamento do serviço
        List<CreditCardBillsDto> billsList = List.of(creditCardBillsDto);
        when(creditCardBillsService.addNewBills(any(), anyInt())).thenReturn(billsList);

        // Mockando o comportamento do UriComponentsBuilder e UriComponents
        UriComponents uriComponents = UriComponentsBuilder.fromPath("/api/v1/bills/1").build(); // Criando um UriComponents

        // Simulando os métodos do UriComponentsBuilder
        when(uriBuilder.path("/api/v1/bills/{id}")).thenReturn(uriBuilder);
        when(uriBuilder.buildAndExpand(creditCardBillsDto.getId())).thenReturn(uriComponents);

        // Simulando o comportamento de toUri() no UriComponents
        when(uriComponents.toUri()).thenReturn(uriComponents.toUri());

        // Chama o método do controller passando o mock de UriComponentsBuilder
        ResponseEntity<List<CreditCardBillsDto>> response = creditCardBillsController.createBills(creditCardBillsDto, 1, uriBuilder);

        // Verifica o status da resposta e o corpo
        assertNotNull(response);
        assertEquals(201, response.getStatusCode().value());
        assertEquals(billsList, response.getBody());
    }


    @Test
    void deleteBill_ShouldReturnNoContent_WhenBillDeletedSuccessfully() {
        doNothing().when(creditCardBillsService).deleteBillById(1L);

        ResponseEntity<Void> response = creditCardBillsController.deleteBill(1L);

        assertEquals(204, response.getStatusCode().value());
    }

    @Test
    void deleteBill_ShouldReturnNotFound_WhenBillDoesNotExist() {
        doThrow(new EntityNotFoundException()).when(creditCardBillsService).deleteBillById(1L);

        ResponseEntity<Void> response = creditCardBillsController.deleteBill(1L);

        assertEquals(404, response.getStatusCode().value());
    }
}
