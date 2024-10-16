package controller;

import entity.Notification;
import exceptions.NoSuchNotificationException;
import exceptions.NotificationNotYoursException;
import response.NotificationsPack;
import service.NotificationManagementService;
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
    
    private final NotificationManagementService notificationmanagementService;
    private final JwtTokenManager jwtTokenManager;

    @Autowired
    public NotificationController(
        NotificationManagementService notificationmanagementService,
        JwtTokenManager jwtTokenManager
    ) {
        this.notificationmanagementService = notificationmanagementService;
        this.jwtTokenManager = jwtTokenManager;
    }

    @RequireJWT
    @GetMapping(value = "/notifications/all", produces = "application/json")
    public ResponseEntity<NotificationsPack> sendNotifications(
        @RequestHeader(name = "Authorization") String authorizationHeader,
        @RequestParam(defaultValue = "1")      Integer page, 
        @RequestParam(defaultValue = "5")      Integer size
    ) 
        throws
            NumberFormatException
    {
        int zeroIndexedPage = page - 1;
        PageRequest pageDescriptor = PageRequest.of(zeroIndexedPage, size);
        String jwtToken = jwtTokenManager.getTokenFromRequestHeader(authorizationHeader);
        String id = jwtTokenManager.getIdFromJWT(jwtToken);
        List<Notification> notifications = notificationmanagementService.findByUser(id, pageDescriptor);
        long readNotifications = notificationmanagementService.countReadNotifications(id);
        long unreadNotifications = notificationmanagementService.countUnreadNotifications(id);
        NotificationsPack notificationsPack = new NotificationsPack(notifications, readNotifications, unreadNotifications);
        return ResponseEntity.ok().body(notificationsPack);
    }

    @RequireJWT
    @Transactional
    @PostMapping(value = "/notifications/mark-all-as-read", produces = "text/plain")
    public ResponseEntity<String> markAllNotificationsAsRead(
        @RequestHeader(name = "Authorization") String authorizationHeader
    ) {
        String jwtToken = jwtTokenManager.getTokenFromRequestHeader(authorizationHeader);
        String id = jwtTokenManager.getIdFromJWT(jwtToken);
        notificationmanagementService.markAllNotificationsAsRead(id);
        return ResponseEntity.ok().body("done");
    }

    @RequireJWT
    @Transactional
    @DeleteMapping(value = "/notifications/mark-all-as-eliminated", produces = "text/plain")
    public ResponseEntity<String> markAllNotificationsAsEliminated(
        @RequestHeader(name = "Authorization") String authorizationHeader
    ) {
        String jwtToken = jwtTokenManager.getTokenFromRequestHeader(authorizationHeader);
        String id = jwtTokenManager.getIdFromJWT(jwtToken);
        notificationmanagementService.markAllNotificationsAsEliminated(id);
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
            NotificationNotYoursException
    {
        Notification notification = notificationmanagementService.findByNotificationId(notificationId);
        ensureNotificationOwnership(notification, authorizationHeader);
        notificationmanagementService.markNotificationAsRead(notificationId);
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
            NotificationNotYoursException
    {
        Notification notification = notificationmanagementService.findByNotificationId(notificationId);
        ensureNotificationOwnership(notification, authorizationHeader);
        notificationmanagementService.markNotificationAsEliminated(notificationId);
        return ResponseEntity.ok().body("done");
    }

    public void ensureNotificationOwnership(Notification notification, String authorizationHeader)
        throws 
            NotificationNotYoursException
    {
        String jwtToken = jwtTokenManager.getTokenFromRequestHeader(authorizationHeader);
        String id = jwtTokenManager.getIdFromJWT(jwtToken);
        if (!notification.getAccountId().toString().equals(id)){
            throw new NotificationNotYoursException();
        }
    }
}
