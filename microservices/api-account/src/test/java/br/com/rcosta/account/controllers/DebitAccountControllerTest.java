package br.com.rcosta.account.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import br.com.rcosta.account.controller.DebitAccountController;
import br.com.rcosta.account.dto.DebitAccountDto;
import br.com.rcosta.account.services.DebitAccountService;

public class DebitAccountControllerTest {

	@Mock
    private DebitAccountService debitAccountService;

    @InjectMocks
    private DebitAccountController debitAccountController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @SuppressWarnings("deprecation")
	@Test
    void shouldReturnDebitById() {
        // Arrange
        DebitAccountDto dto = new DebitAccountDto();
        when(debitAccountService.getDebitById(1L)).thenReturn(dto);

        // Act
        ResponseEntity<DebitAccountDto> response = debitAccountController.findDebitById(1L);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(dto, response.getBody());
    }

    @SuppressWarnings("deprecation")
	@Test
    void shouldReturnAllDebitsByPersonalAccountId() {
        // Arrange
        List<DebitAccountDto> debitList = List.of(new DebitAccountDto());
        when(debitAccountService.getDebitByPersonalAccountId(1L)).thenReturn(debitList);

        // Act
        ResponseEntity<List<DebitAccountDto>> response = debitAccountController.findByPersonalAccountId(1L);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(debitList, response.getBody());
    }
}
