package br.com.rcosta.credit.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "tb_credit_card_bills")
public class CreditCardBillsModel implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	private String name;
	private LocalDate date;
	
	@NotNull
	private Integer paymentMonth;
	
	@NotNull
	private Integer paymentYear;
	
	@NotNull
	private Double price;
	
	@NotNull
	private Boolean isParcel;
	
	@ManyToOne(cascade = CascadeType.PERSIST, targetEntity = CreditCardModel.class)
	private CreditCardModel creditCard;

	public CreditCardBillsModel(Long id, String name, LocalDate date, Integer paymentMonth,
			Integer paymentYear, Double price, Boolean isParcel, CreditCardModel creditCard) {
		this.id = id;
		this.name = name;
		this.date = date;
		this.paymentMonth = paymentMonth;
		this.paymentYear = paymentYear;
		this.price = price;
		this.isParcel = isParcel;
	}
	
	public CreditCardBillsModel() { }

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

	public CreditCardModel getCreditCard() {
		return creditCard;
	}

	public void setCreditCard(CreditCardModel creditCard) {
		this.creditCard = creditCard;
	}
	
	public void setPriceWithParcelDivision(int quantity) {
		if (quantity > 0) {
			this.price = this.price / quantity;  // Atualiza o preço para cada parcela
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(creditCard, date, id, isParcel, name, paymentMonth, paymentYear, price);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CreditCardBillsModel other = (CreditCardBillsModel) obj;
		return Objects.equals(creditCard, other.creditCard) && Objects.equals(date, other.date)
				&& Objects.equals(id, other.id) && Objects.equals(isParcel, other.isParcel)
				&& Objects.equals(name, other.name) && Objects.equals(paymentMonth, other.paymentMonth)
				&& Objects.equals(paymentYear, other.paymentYear) && Objects.equals(price, other.price);
	}
}
