package repository;

import entity.NotificationData;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface NotificationDataRepository extends JpaRepository<NotificationData, Integer> {
    
    Optional<NotificationData> findById(Integer id);

    @Modifying
    @Query("UPDATE NotificationData n SET n.visualized = :visualized WHERE n.id = :notificationId")
    void updateVisualizedByNotificationId(boolean visualized, int notificationId);

    @Modifying
    @Query("UPDATE NotificationData n SET n.eliminated = :eliminated WHERE n.id = :notificationId")
    void updateEliminatedByNotificationId(boolean eliminated, int notificationId);

    @Modifying
    @Query("UPDATE NotificationData n SET n.visualized = :visualized WHERE n.accountId = :accountId")
    void updateVisualizedByAccountId(boolean visualized, Integer accountId);

    @Modifying
    @Query("UPDATE NotificationData n SET n.eliminated = :eliminated WHERE n.accountId = :accountId")
    void updateEliminatedByAccountId(boolean eliminated, Integer accountId);
}
