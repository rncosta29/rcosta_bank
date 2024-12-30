package br.com.rcosta.account.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_debit_account")
public class DebitAccountModel implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String institution;
	private LocalDate date;
	private Double price;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "personal_account_id")
    private PersonalAccountModel personalAccount;
	
	public DebitAccountModel() { }

	public DebitAccountModel(Long id, String institution, LocalDate date, Double price) {
		this.id = id;
		this.institution = institution;
		this.date = date;
		this.price = price;
	}

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
        if (personalAccount != null) {
            personalAccount.updateBalance();
        }
    }

    public PersonalAccountModel getPersonalAccount() {
        return personalAccount;
    }

    public void setPersonalAccount(PersonalAccountModel personalAccount) {
        this.personalAccount = personalAccount;
        if (personalAccount != null) {
            personalAccount.updateBalance();
        }
    }

	@Override
	public int hashCode() {
		return Objects.hash(date, id, institution, personalAccount, price);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DebitAccountModel other = (DebitAccountModel) obj;
		return Objects.equals(date, other.date) && Objects.equals(id, other.id)
				&& Objects.equals(institution, other.institution)
				&& Objects.equals(personalAccount, other.personalAccount)
				&& Objects.equals(price, other.price);
	}
}
