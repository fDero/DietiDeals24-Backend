
package repository;

import entity.Password;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface PasswordRepository extends JpaRepository<Password, Integer> {

    Optional<Password> findPasswordByAccountId(int accountId);
}
