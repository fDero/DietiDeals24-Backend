package controller;

import entity.Notification;
import repository.NotificationRepository;
import response.AccountProfileInformations;
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
    public ResponseEntity<AccountProfileInformations> sendProfileInformations(@RequestParam String email) 
    {
        List<Notification> notification = notificationRepository.findByEmail(email);
        return null;
    }
}
