package repository;

import entity.Iban;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IbanRepository extends JpaRepository<Iban, Integer> {

    List<Iban> findByAccountId(Integer accountId);
}