package br.com.rcosta.account.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;


import java.net.URI;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.rcosta.account.controller.PersonalAccountController;
import br.com.rcosta.account.dto.DebitAccountDto;
import br.com.rcosta.account.dto.PersonalAccountDto;
import br.com.rcosta.account.services.PersonalAccountService;

class PersonalAccountControllerTest {

	@Mock
    private PersonalAccountService personalAccountService;

    @InjectMocks
    private PersonalAccountController personalAccountController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCreditCard() {
        // Arrange
        PersonalAccountDto personalAccountDto = new PersonalAccountDto();
        personalAccountDto.setId(1L);
        
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance();
        when(personalAccountService.addPersonalAccount(personalAccountDto)).thenReturn(personalAccountDto);
        
        // Act
        ResponseEntity<PersonalAccountDto> response = personalAccountController.createCreditCard(personalAccountDto, uriBuilder);

        // Assert
        assertNotNull(response);
        assertEquals(201, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        URI location = response.getHeaders().getLocation();
        assertNotNull(location);
        assertTrue(location.getPath().contains("1")); // Verifica se o id está presente na URL
    }

    @Test
    void testFindCreditCardById() {
        // Arrange
        Long id = 1L;
        PersonalAccountDto personalAccountDto = new PersonalAccountDto();
        personalAccountDto.setId(id);
        
        when(personalAccountService.getPersonalAccountById(id)).thenReturn(personalAccountDto);
        
        // Act
        ResponseEntity<PersonalAccountDto> response = personalAccountController.findCreditCardById(id);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(id, response.getBody().getId());
    }

    @Test
    void testAddDebitToPersonalAccount() {
        // Arrange
        Long id = 1L;
        DebitAccountDto debitAccountDto = new DebitAccountDto();
        debitAccountDto.setId(2L);

        // Usando matchers para ambos os parâmetros
        when(personalAccountService.addDebitToPersonalAccount(eq(id), any(DebitAccountDto.class))).thenReturn(debitAccountDto);

        // Act
        ResponseEntity<DebitAccountDto> response = personalAccountController.addDebitToPersonalAccount(id, debitAccountDto);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value()); // Atualizado para não usar o método deprecated
        assertNotNull(response.getBody());
        assertEquals(2L, response.getBody().getId());
    }

}
