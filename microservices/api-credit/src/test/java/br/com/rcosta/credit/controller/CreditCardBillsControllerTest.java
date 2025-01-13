package br.com.rcosta.credit.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
import org.springframework.web.util.UriComponentsBuilder;

import br.com.rcosta.credit.dto.CreditCardBillsDto;
import br.com.rcosta.credit.services.CreditCardBillsService;
import jakarta.persistence.EntityNotFoundException;

public class CreditCardBillsControllerTest {

    @InjectMocks
    private CreditCardBillsController creditCardBillsController;

    @Mock
    private CreditCardBillsService creditCardBillsService;

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
        List<CreditCardBillsDto> billsList = List.of(creditCardBillsDto);
        when(creditCardBillsService.addNewBills(any(), anyInt())).thenReturn(billsList);

        ResponseEntity<List<CreditCardBillsDto>> response = creditCardBillsController.createBills(
            creditCardBillsDto, 
            1, 
            UriComponentsBuilder.newInstance()
        );

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
    
    @Test
    void findAllBills_ShouldReturnEmptyList_WhenNoBillsExist() {
        // Arrange
        when(creditCardBillsService.allBills()).thenReturn(List.of());

        // Act
        ResponseEntity<List<CreditCardBillsDto>> response = creditCardBillsController.findAllBills();

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void createBills_ShouldReturnInternalServerError_WhenExceptionOccurs() {
        // Arrange
        when(creditCardBillsService.addNewBills(any(), anyInt()))
            .thenThrow(new RuntimeException("Unexpected error"));

        // Act
        ResponseEntity<List<CreditCardBillsDto>> response = creditCardBillsController.createBills(
            creditCardBillsDto, 
            1, 
            UriComponentsBuilder.newInstance()
        );

        // Assert
        assertNotNull(response);
        assertEquals(500, response.getStatusCode().value());
        assertEquals(null, response.getBody());
    }

    @Test
    void findBillsById_ShouldReturnBill_WhenFound() {
        // Arrange
        when(creditCardBillsService.getBillsById(1L)).thenReturn(creditCardBillsDto);

        // Act
        ResponseEntity<CreditCardBillsDto> response = creditCardBillsController.findBillsById(1L);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(creditCardBillsDto, response.getBody());
    }

    @Test
    void findBillsById_ShouldReturnNotFound_WhenBillDoesNotExist() {
        // Arrange
        when(creditCardBillsService.getBillsById(1L)).thenThrow(new EntityNotFoundException());

        // Act
        ResponseEntity<CreditCardBillsDto> response = creditCardBillsController.findBillsById(1L);

        // Assert
        assertNotNull(response);
        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    void findByCreditCardId_ShouldReturnEmptyList_WhenNoBillsForCreditCard() {
        // Arrange
        when(creditCardBillsService.getBillsByCreditCardId(1L)).thenReturn(List.of());

        // Act
        ResponseEntity<List<CreditCardBillsDto>> response = creditCardBillsController.findByCreditCardId(1L);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void deleteBill_ShouldReturnInternalServerError_WhenExceptionOccurs() {
        // Arrange
        doThrow(new RuntimeException("Unexpected error")).when(creditCardBillsService).deleteBillById(1L);

        // Act
        ResponseEntity<Void> response = creditCardBillsController.deleteBill(1L);

        // Assert
        assertEquals(500, response.getStatusCode().value());
    }

    @Test
    void createBills_ShouldGenerateCorrectURI_WhenBillsAreCreated() {
        // Arrange
        List<CreditCardBillsDto> billsList = List.of(creditCardBillsDto);
        when(creditCardBillsService.addNewBills(any(), anyInt())).thenReturn(billsList);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance();

        // Act
        ResponseEntity<List<CreditCardBillsDto>> response = creditCardBillsController.createBills(
            creditCardBillsDto, 
            1, 
            uriBuilder
        );

        // Assert
        assertNotNull(response);
        assertEquals(201, response.getStatusCode().value());
        assertEquals("/api/v1/bills/1", uriBuilder.build().toUriString());  // Verifica a URI gerada
    }
}
