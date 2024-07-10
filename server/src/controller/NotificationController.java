package controller;

import entity.Notification;
import exceptions.NoSuchNotificationException;
import exceptions.NotificationNotYoursException;

import repository.NotificationRepository;
import response.NotificationsPack;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import java.util.List;
import authentication.JwtTokenManager;

import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<NotificationsPack> sendProfileInformations(@RequestHeader(name = "Authorization") String authorizationHeader) {
        String jwtToken = jwtTokenProvider.getTokenFromRequestHeader(authorizationHeader);
        String email = jwtTokenProvider.getEmailFromJWT(jwtToken);
        List<Notification> notifications = notificationRepository.findByEmail(email);
        return ResponseEntity.ok().body(new NotificationsPack(notifications));
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
        String jwtToken = jwtTokenProvider.getTokenFromRequestHeader(authorizationHeader);
        String email = jwtTokenProvider.getEmailFromJWT(jwtToken);
        Notification notification = notificationRepository.findById(notificationId)
            .orElseThrow(() -> new NoSuchNotificationException());
        if (notification.getEmail() != email){
            throw new NotificationNotYoursException();
        }
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
            NotificationNotYoursException
    {
        String jwtToken = jwtTokenProvider.getTokenFromRequestHeader(authorizationHeader);
        String email = jwtTokenProvider.getEmailFromJWT(jwtToken);
        Optional<Notification> notification = notificationRepository.findById(notificationId);
        if (!notification.isPresent()){
            throw new NoSuchNotificationException();
        }
        if (notification.get().getEmail() != email){
            throw new NotificationNotYoursException();
        }
        notificationRepository.markNotificationAsRead(notificationId);
        return ResponseEntity.ok().body("done");
    }
}
