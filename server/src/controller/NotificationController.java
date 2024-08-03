package controller;

import entity.Notification;
import exceptions.NoAccountWithSuchEmailException;
import exceptions.NoSuchNotificationException;
import exceptions.NotificationNotYoursException;
import repository.NotificationRepository;
import response.NotificationsPack;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import java.util.List;
import authentication.JwtTokenManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import authentication.RequireJWT;
import jakarta.transaction.Transactional;


@RestController
public class NotificationController {
    
    private final NotificationRepository notificationRepository;
    private final JwtTokenManager jwtTokenProvider;

    @Autowired
    public NotificationController(
        NotificationRepository notificationRepository,
        JwtTokenManager jwtTokenProvider
    ) {
        this.notificationRepository = notificationRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @RequireJWT
    @GetMapping(value = "/notifications/all", produces = "application/json")
    public ResponseEntity<NotificationsPack> sendNotifications(
        @RequestHeader(name = "Authorization") String authorizationHeader,
        @RequestParam(defaultValue = "1")      Integer page, 
        @RequestParam(defaultValue = "5")      Integer size
    ) 
        throws 
            NoAccountWithSuchEmailException, 
            NumberFormatException
    {
        int zeroIndexedPage = page - 1;
        PageRequest pageDescriptor = PageRequest.of(zeroIndexedPage, size);
        String jwtToken = jwtTokenProvider.getTokenFromRequestHeader(authorizationHeader);
        String id = jwtTokenProvider.getIdFromJWT(jwtToken);
        List<Notification> notifications = notificationRepository.findByAccountId(Integer.valueOf(id), pageDescriptor);
        long readNotifications = notificationRepository.countReadNotifications(Integer.valueOf(id));
        long unreadNotifications = notificationRepository.countUnreadNotifications(Integer.valueOf(id));
        NotificationsPack notificationsPack = new NotificationsPack(notifications, readNotifications, unreadNotifications);
        return ResponseEntity.ok().body(notificationsPack);
    }

    @RequireJWT
    @Transactional
    @PostMapping(value = "/notifications/mark-all-as-read", produces = "text/plain")
    public ResponseEntity<String> markAllNotificationsAsRead(
        @RequestHeader(name = "Authorization") String authorizationHeader
    ) 
        throws
            NoSuchNotificationException,
            NotificationNotYoursException,
            NoAccountWithSuchEmailException
    {
        String jwtToken = jwtTokenProvider.getTokenFromRequestHeader(authorizationHeader);
        String id = jwtTokenProvider.getIdFromJWT(jwtToken);
        notificationRepository.markAllNotificationsAsRead(Integer.valueOf(id));
        return ResponseEntity.ok().body("done");
    }

    @RequireJWT
    @Transactional
    @DeleteMapping(value = "/notifications/mark-all-as-eliminated", produces = "text/plain")
    public ResponseEntity<String> markAllNotificationsAsEliminated(
        @RequestHeader(name = "Authorization") String authorizationHeader
    ) 
        throws
            NoSuchNotificationException,
            NotificationNotYoursException,
            NoAccountWithSuchEmailException
    {
        String jwtToken = jwtTokenProvider.getTokenFromRequestHeader(authorizationHeader);
        String id = jwtTokenProvider.getIdFromJWT(jwtToken);
        notificationRepository.markAllNotificationsAsEliminated(Integer.valueOf(id));
        return ResponseEntity.ok().body("done");
    }

    @RequireJWT
    @Transactional
    @PostMapping(value = "/notifications/mark-as-read", produces = "text/plain")
    public ResponseEntity<String> markNotificationAsRead(
        @RequestHeader(name = "Authorization") String authorizationHeader,
        @RequestParam(name = "notificationId") int notificationId
    ) 
        throws
            NoSuchNotificationException,
            NotificationNotYoursException,
            NoAccountWithSuchEmailException
    {
        Notification notification = notificationRepository.findById(notificationId)
            .orElseThrow(() -> new NoSuchNotificationException());
        ensureNotificationOwnership(notification, authorizationHeader);
        notificationRepository.markNotificationAsRead(notificationId);
        return ResponseEntity.ok().body("done");
    }

    
    @RequireJWT
    @Transactional
    @DeleteMapping(value = "/notifications/mark-as-eliminated", produces = "text/plain")
    public ResponseEntity<String> markNotificationAsEliminated(
        @RequestHeader(name = "Authorization") String authorizationHeader,
        @RequestParam(name = "notificationId") int notificationId
    ) 
        throws
            NoSuchNotificationException,
            NotificationNotYoursException,
            NoAccountWithSuchEmailException
    {
        
        Optional<Notification> notification = notificationRepository.findById(notificationId);
        if (!notification.isPresent()){
            throw new NoSuchNotificationException();
        }
        ensureNotificationOwnership(notification.get(), authorizationHeader);
        notificationRepository.markNotificationAsRead(notificationId);
        return ResponseEntity.ok().body("done");
    }

    public void ensureNotificationOwnership(Notification notification, String authorizationHeader)
        throws 
            NotificationNotYoursException,
            NoAccountWithSuchEmailException
    {
        String jwtToken = jwtTokenProvider.getTokenFromRequestHeader(authorizationHeader);
        String id = jwtTokenProvider.getIdFromJWT(jwtToken);
        if (!notification.getAccountId().toString().equals(id)){
            throw new NotificationNotYoursException();
        }
    }
}
