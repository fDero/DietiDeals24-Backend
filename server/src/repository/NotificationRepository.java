package repository;

import entity.Notification;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    
    List<Notification> findByAccountUsername(String username, Pageable pageable);
    Optional<Notification> findById(Integer id);

    @Query("UPDATE Notification n SET n.visualized = true WHERE n.id = :notificationId")
    void markNotificationAsRead(int notificationId);

    @Query("UPDATE Notification n SET n.eliminated = true WHERE n.id = :notificationId")
    void markNotificationAsElimintaed(int notificationId);
}
