package repository;

import entity.PersonalLink;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalLinkRepository extends JpaRepository<PersonalLink, Integer> {

    List<PersonalLink> findByAccountId(int accountId);
}
