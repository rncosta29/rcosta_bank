package br.com.rcosta.credit.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.rcosta.credit.dto.CreditCardDto;
import br.com.rcosta.credit.services.CreditCardService;

@RestController
@RequestMapping("/api/v1/credit-card")
@CrossOrigin(origins = "*")
public class CreditCardController {

	private CreditCardService creditCardService;
	
	public CreditCardController(CreditCardService creditCardService) {
		this.creditCardService = creditCardService;
	}
	
	@GetMapping()
	public ResponseEntity<List<CreditCardDto>> findAllCreditCard() {
		return ResponseEntity.ok(creditCardService.allCreditsCard());
	}
	
	@PostMapping("/insert")
	public ResponseEntity<CreditCardDto> createCreditCard(@RequestBody CreditCardDto dto, UriComponentsBuilder uriBuilder) {
		CreditCardDto model = creditCardService.addCreditCard(dto);
		URI address = uriBuilder.path("/api/v1/credit-card/{id}").buildAndExpand(model.getId()).toUri();
		
		return ResponseEntity.created(address).body(model);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<CreditCardDto> findCreditCardById(@PathVariable Long id) {
		CreditCardDto dto = creditCardService.getCreditsCardById(id);
		
		return ResponseEntity.ok(dto);
	}
}
