package repository;

import entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

import org.springframework.stereotype.Repository;


@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

        boolean existsAccountByEmail(String email);

        boolean existsAccountByUsername(String username);
        
        Optional<Account> findAccountByEmail(String email);

        Optional<Account> findAccountByUsername(String username);        
}
