package br.com.rcosta.credit.dto;

import java.time.LocalDate;

public class CreditCardBillsDto {

	private Long id;
	private String name;
	private LocalDate date;
	private Double price;
	private Boolean isParcel;
	private Integer paymentMonth;
	private Integer paymentYear;
	private Long creditCardId;
	private CreditCardDto creditCardDto;
	
	public CreditCardBillsDto() { }

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

	public Boolean getIsParcel() {
		return isParcel;
	}

	public void setIsParcel(Boolean isParcel) {
		this.isParcel = isParcel;
	}

	public Integer getPaymentMonth() {
		return paymentMonth;
	}

	public void setPaymentMonth(Integer paymentMonth) {
		this.paymentMonth = paymentMonth;
	}

	public Integer getPaymentYear() {
		return paymentYear;
	}

	public void setPaymentYear(Integer paymentYear) {
		this.paymentYear = paymentYear;
	}

	public CreditCardDto getCreditCardDto() {
		return creditCardDto;
	}

	public void setCreditCardDto(CreditCardDto creditCardDto) {
		this.creditCardDto = creditCardDto;
	}

	public Long getCreditCardId() {
		return creditCardId;
	}

	public void setCreditCardId(Long creditCardId) {
		this.creditCardId = creditCardId;
	}
}
