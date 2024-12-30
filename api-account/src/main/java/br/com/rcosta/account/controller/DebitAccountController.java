package br.com.rcosta.account.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.rcosta.account.dto.DebitAccountDto;
import br.com.rcosta.account.services.DebitAccountService;
import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/v1/debit")
@CrossOrigin(origins = "*")
public class DebitAccountController {

	private DebitAccountService debitAccountService;
	
	public DebitAccountController(DebitAccountService debitAccountService) {
		this.debitAccountService = debitAccountService;
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<DebitAccountDto> findDebitById(@PathVariable Long id) {
	    // Busca o débito pelo ID
	    DebitAccountDto dto = debitAccountService.getDebitById(id);
	    return ResponseEntity.ok(dto);
	}

	@GetMapping("/personal-account-id/{id}")
	public ResponseEntity<List<DebitAccountDto>> findByPersonalAccountId(@PathVariable Long id) {
	    // Busca todos os débitos associados à conta pessoal
	    List<DebitAccountDto> list = debitAccountService.getDebitByPersonalAccountId(id);
	    return ResponseEntity.ok(list);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteDebit(@PathVariable Long id) {
	    try {
	        // Chama o serviço para excluir o débito e atualizar o saldo
	        debitAccountService.deleteDebitById(id);

	        // Retorna status 204 (No Content) caso a exclusão seja bem-sucedida
	        return ResponseEntity.noContent().build();
	    } catch (EntityNotFoundException e) {
	        // Retorna status 404 (Not Found) se o débito não for encontrado
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	    } catch (Exception e) {
	        e.printStackTrace();
	        // Retorna status 500 (Internal Server Error) para erros inesperados
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<DebitAccountDto> updateDebit(@PathVariable Long id, @RequestBody DebitAccountDto dto) {
	    try {
	        // Atualiza o débito e recalcula o saldo
	        DebitAccountDto updatedDebit = debitAccountService.updateDebit(id, dto);

	        // Retorna o débito atualizado
	        return ResponseEntity.ok(updatedDebit);
	    } catch (EntityNotFoundException e) {
	        // Retorna status 404 se o débito não for encontrado
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	    } catch (Exception e) {
	        e.printStackTrace();
	        // Retorna status 500 para erros inesperados
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	}
}
