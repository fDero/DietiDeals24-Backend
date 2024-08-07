package service;

import java.util.List;
import entity.Auction;
import org.springframework.data.domain.Pageable;
import entity.Bid;
import entity.Notification;
import entity.NotificationData;
import exceptions.NoSuchNotificationException;
import repository.NotificationDataRepository;
import repository.NotificationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationManagementService {
    
    private final NotificationRepository notificationRepository;
    private final NotificationDataRepository notificationDataRepository;
    
    @Autowired
    public NotificationManagementService(
        NotificationRepository notificationRepository,
        NotificationDataRepository notificationDataRepository
    ) {
        this.notificationRepository = notificationRepository;
        this.notificationDataRepository = notificationDataRepository;
    }

    public List<Notification> findByUser(String accountId, Pageable pageable) {
        int id = Integer.parseInt(accountId);
        return notificationRepository.findByAccountId(id, pageable);
    }

    public Notification findByNotificationId(int id) 
        throws NoSuchNotificationException
    {
        return notificationRepository.findById(id)
            .orElseThrow(() -> new NoSuchNotificationException());
    }
    
    public long countReadNotifications(String accountId) {
        int id = Integer.parseInt(accountId);
        return notificationRepository.countByAccountIdAndVisualizedAndEliminated(id, true, false);
    }

    public long countUnreadNotifications(String accountId) {
        int id = Integer.parseInt(accountId);
        return notificationRepository.countByAccountIdAndVisualizedAndEliminated(id, false, false);
    }

    public void markNotificationAsRead(int notificationId) {
        notificationDataRepository.updateVisualizedByNotificationId(true, notificationId);
    }

    public void markNotificationAsEliminated(int notificationId) {
        notificationDataRepository.updateEliminatedByNotificationId(true, notificationId);
    }

    public void markAllNotificationsAsRead(String accountId) {
        int id = Integer.parseInt(accountId);
        notificationDataRepository.updateVisualizedByAccountId(true, id);
    }

    public void markAllNotificationsAsEliminated(String accountId) {
        int id = Integer.parseInt(accountId);
        notificationDataRepository.updateEliminatedByAccountId(true, id);
    }

    public void notifyUserOfNewBid(Bid bid, Auction auction) {
        NotificationData notification = new NotificationData();
        notification.setAccountId(bid.getAuctionId());
        notification.setAuctionId(auction.getCreatorId());
        notification.setNotificationType("new-bid");
        notification.setVisualized(false);
        notification.setEliminated(false);
        notificationDataRepository.save(notification);
    }
}
