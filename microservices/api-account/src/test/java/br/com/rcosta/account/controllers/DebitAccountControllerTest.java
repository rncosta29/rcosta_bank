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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.com.rcosta.account.controller.DebitAccountController;
import br.com.rcosta.account.dto.DebitAccountDto;
import br.com.rcosta.account.services.DebitAccountService;

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
        dto.setId(1L); // Adicionando dados para o DTO
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
        dto.setId(1L); // Adicionando dados para o DTO
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
}
