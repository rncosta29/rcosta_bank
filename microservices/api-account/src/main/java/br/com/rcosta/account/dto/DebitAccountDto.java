package br.com.rcosta.account.dto;

import java.time.LocalDate;

public class DebitAccountDto {

	private Long id;
	private String institution;
	private LocalDate date;
	private Double price;
	private Long personalAccountId;
	private PersonalAccountDto personalAccountDto;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getInstitution() {
		return institution;
	}

	public void setInstitution(String institution) {
		this.institution = institution;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Long getPersonalAccountId() {
		return personalAccountId;
	}

	public void setPersonalAccountId(Long personalAccountId) {
		this.personalAccountId = personalAccountId;
	}

	public PersonalAccountDto getPersonalAccountDto() {
		return personalAccountDto;
	}

	public void setPersonalAccountDto(PersonalAccountDto personalAccountDto) {
		this.personalAccountDto = personalAccountDto;
	}
}
