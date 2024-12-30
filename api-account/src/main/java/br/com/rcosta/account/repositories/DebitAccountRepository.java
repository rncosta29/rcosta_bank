package br.com.rcosta.account.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.rcosta.account.models.DebitAccountModel;

@Repository
public interface DebitAccountRepository extends JpaRepository<DebitAccountModel, Long> {
	@Query("SELECT c FROM DebitAccountModel c WHERE c.personalAccount.id = :personalAccountId")
	List<DebitAccountModel> findByPersonalAccountId(@Param("personalAccountId") Long personalAccountId);
}
