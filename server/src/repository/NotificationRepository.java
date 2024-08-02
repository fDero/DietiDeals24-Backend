package repository;

import entity.Notification;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    
    List<Notification> findByAccountId(Integer username, Pageable pageable);
    Optional<Notification> findById(Integer id);

    @Modifying
    @Query("UPDATE NotificationData n SET n.visualized = true WHERE n.id = :notificationId")
    void markNotificationAsRead(int notificationId);

    @Modifying
    @Query("UPDATE NotificationData n SET n.eliminated = true WHERE n.id = :notificationId")
    void markNotificationAsElimintaed(int notificationId);

    @Modifying
    @Query("UPDATE NotificationData n SET n.visualized = true WHERE n.accountId = :accountId")
    void markAllNotificationsAsRead(Integer accountId);

    @Modifying
    @Query("UPDATE NotificationData n SET n.eliminated = true WHERE n.accountId = :accountId")
    void markAllNotificationsAsEliminated(Integer accountId);

    long countByAccountIdAndVisualizedAndEliminated(Integer accountId, boolean visualized, boolean eliminated);

    default long countUnreadNotifications(Integer accountId) {
        return countByAccountIdAndVisualizedAndEliminated(accountId, false, false);
    }

    default long countReadNotifications(Integer accountId) {
        return countByAccountIdAndVisualizedAndEliminated(accountId, true, false);
    }
}
