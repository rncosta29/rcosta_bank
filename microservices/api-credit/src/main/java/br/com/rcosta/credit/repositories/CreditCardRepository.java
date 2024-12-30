package br.com.rcosta.credit.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.rcosta.credit.models.CreditCardModel;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCardModel, Long> {

}
