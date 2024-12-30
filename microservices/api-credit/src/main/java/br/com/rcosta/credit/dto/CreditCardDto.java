package br.com.rcosta.credit.dto;

import java.util.List;

public class CreditCardDto {

	private Long id;	
	private String name;
	private List<CreditCardBillsDto> creditsCardDto;
	
	public CreditCardDto() { }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<CreditCardBillsDto> getCreditsCardDto() {
		return creditsCardDto;
	}

	public void setCreditsCardDto(List<CreditCardBillsDto> creditsCardDto) {
		this.creditsCardDto = creditsCardDto;
	}
}
