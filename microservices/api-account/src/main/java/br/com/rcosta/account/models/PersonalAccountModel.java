package br.com.rcosta.account.models;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_personal_account")
public class PersonalAccountModel implements Serializable {

private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String nameAccount;
	private Double balance;
	
	@JsonIgnore
    @OneToMany(mappedBy = "personalAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DebitAccountModel> debitAccounts;
	
	public PersonalAccountModel() { }
	
	public PersonalAccountModel(Long id, String nameAccount, Double balance) {
		this.id = id;
		this.nameAccount = nameAccount;
		this.balance = balance;
	}

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
	
	public List<DebitAccountModel> getDebitAccounts() {
        return debitAccounts;
    }

    public void setDebitAccounts(List<DebitAccountModel> debitAccounts) {
        this.debitAccounts = debitAccounts;
        updateBalance();
    }

    public void updateBalance() {
        this.balance = debitAccounts != null
            ? debitAccounts.stream()
                .filter(debit -> debit.getPrice() != null) // Ignora débitos com valores nulos
                .mapToDouble(DebitAccountModel::getPrice)
                .sum()
            : 0.0;
    }

	@Override
	public int hashCode() {
		return Objects.hash(balance, id, nameAccount);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PersonalAccountModel other = (PersonalAccountModel) obj;
		return Objects.equals(balance, other.balance) && Objects.equals(id, other.id)
				&& Objects.equals(nameAccount, other.nameAccount);
	}
}
