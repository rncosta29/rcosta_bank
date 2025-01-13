package br.com.rcosta.account.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.com.rcosta.account.controller.DebitAccountController;
import br.com.rcosta.account.dto.DebitAccountDto;
import br.com.rcosta.account.services.DebitAccountService;
import jakarta.persistence.EntityNotFoundException;

class DebitAccountControllerTest {

	 @Mock
	    private DebitAccountService debitAccountService;

	    @InjectMocks
	    private DebitAccountController debitAccountController;

	    @BeforeEach
	    void setUp() {
	        MockitoAnnotations.openMocks(this);
	    }

	    @Test
	    void shouldReturnDebitById() {
	        // Arrange
	        DebitAccountDto dto = new DebitAccountDto();
	        dto.setId(1L);
	        when(debitAccountService.getDebitById(1L)).thenReturn(dto);

	        // Act
	        ResponseEntity<DebitAccountDto> response = debitAccountController.findDebitById(1L);

	        // Assert
	        assertNotNull(response);
	        assertEquals(HttpStatus.OK, response.getStatusCode());
	        assertNotNull(response.getBody());
	        assertEquals(dto, response.getBody());
	    }

	    @Test
	    void shouldReturnAllDebitsByPersonalAccountId() {
	        // Arrange
	        DebitAccountDto dto = new DebitAccountDto();
	        dto.setId(1L);
	        List<DebitAccountDto> debitList = List.of(dto);
	        when(debitAccountService.getDebitByPersonalAccountId(1L)).thenReturn(debitList);

	        // Act
	        ResponseEntity<List<DebitAccountDto>> response = debitAccountController.findByPersonalAccountId(1L);

	        // Assert
	        assertNotNull(response);
	        assertEquals(HttpStatus.OK, response.getStatusCode());
	        assertNotNull(response.getBody());
	        assertEquals(debitList, response.getBody());
	    }

	    @Test
	    void shouldHandleEntityNotFoundExceptionOnDelete() {
	        // Arrange
	        doThrow(new EntityNotFoundException("Not Found")).when(debitAccountService).deleteDebitById(1L);

	        // Act
	        ResponseEntity<Void> response = debitAccountController.deleteDebit(1L);

	        // Assert
	        assertNotNull(response);
	        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	    }

	    @Test
	    void shouldHandleGenericExceptionOnDelete() {
	        // Arrange
	        doThrow(new RuntimeException("Internal Server Error")).when(debitAccountService).deleteDebitById(1L);

	        // Act
	        ResponseEntity<Void> response = debitAccountController.deleteDebit(1L);

	        // Assert
	        assertNotNull(response);
	        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	    }

	    @Test
	    void shouldUpdateDebitSuccessfully() {
	        // Arrange
	        DebitAccountDto dto = new DebitAccountDto();
	        dto.setId(1L);
	        when(debitAccountService.updateDebit(1L, dto)).thenReturn(dto);

	        // Act
	        ResponseEntity<DebitAccountDto> response = debitAccountController.updateDebit(1L, dto);

	        // Assert
	        assertNotNull(response);
	        assertEquals(HttpStatus.OK, response.getStatusCode());
	        assertNotNull(response.getBody());
	        assertEquals(dto, response.getBody());
	    }

	    @Test
	    void shouldHandleEntityNotFoundExceptionOnUpdate() {
	        // Arrange
	        DebitAccountDto dto = new DebitAccountDto();
	        doThrow(new EntityNotFoundException("Not Found")).when(debitAccountService).updateDebit(1L, dto);

	        // Act
	        ResponseEntity<DebitAccountDto> response = debitAccountController.updateDebit(1L, dto);

	        // Assert
	        assertNotNull(response);
	        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	    }

	    @Test
	    void shouldHandleGenericExceptionOnUpdate() {
	        // Arrange
	        DebitAccountDto dto = new DebitAccountDto();
	        doThrow(new RuntimeException("Internal Server Error")).when(debitAccountService).updateDebit(1L, dto);

	        // Act
	        ResponseEntity<DebitAccountDto> response = debitAccountController.updateDebit(1L, dto);

	        // Assert
	        assertNotNull(response);
	        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	    }
}
