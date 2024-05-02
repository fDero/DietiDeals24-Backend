package controller;

import entity.Notification;
import repository.NotificationRepository;
import response.AccountProfileInformations;
import response.NotificationsPack;

import org.springframework.http.ResponseEntity;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class NotificationConroller {
    
    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationConroller(
        NotificationRepository notificationRepository
    ) {
        this.notificationRepository = notificationRepository;
    }

    @GetMapping("/notifications")
    public ResponseEntity<NotificationsPack> sendProfileInformations(@RequestParam String email) {
        List<Notification> notifications = notificationRepository.findByEmail(email);
        return ResponseEntity.ok().body(new NotificationsPack(notifications));
    }
}
