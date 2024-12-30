package br.com.rcosta.account.dto;

import java.util.List;

public class PersonalAccountDto {

	private Long id;
	private String nameAccount;
	private Double balance;
	private List<DebitAccountDto> debitAccountModel;
	
	public PersonalAccountDto() { }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNameAccount() {
		return nameAccount;
	}

	public void setNameAccount(String nameAccount) {
		this.nameAccount = nameAccount;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public List<DebitAccountDto> getDebitAccountModel() {
		return debitAccountModel;
	}

	public void setDebitAccountModel(List<DebitAccountDto> debitAccountModel) {
		this.debitAccountModel = debitAccountModel;
	}
}
