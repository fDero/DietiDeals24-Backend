
package repository;

import entity.Resource;

import java.sql.Timestamp;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Integer> {

    @Query(value = "SELECT r FROM Resource r WHERE r.confirmationTimestamp = NULL AND r.uploadTimestamp < :resourceExpiration")
    List<Resource> findObsoleteResources(Timestamp resourceExpiration);

    @Modifying
    @Query(value = "DELETE FROM Resource r WHERE r.confirmationTimestamp = NULL AND r.uploadTimestamp < :resourceExpiration")
    void deleteObsoleteResources(Timestamp resourceExpiration);
}
