package repository;

import entity.CreditCard;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, Integer> {

    List<CreditCard> findByAccountId(Integer accountId);
}