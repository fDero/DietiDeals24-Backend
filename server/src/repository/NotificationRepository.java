package repository;

import entity.Notification;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    
    @Query("SELECT n FROM Notification n WHERE n.accountId = :accountId AND n.eliminated = false ORDER BY n.id DESC")
    List<Notification> findByAccountId(Integer accountId, Pageable pageable);

    Optional<Notification> findById(Integer id);

    long countByAccountIdAndVisualizedAndEliminated(Integer accountId, boolean visualized, boolean eliminated);
}
