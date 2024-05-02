package controller;

import entity.Account;
import entity.ContactInformation;
import entity.Notification;
import entity.PersonalLink;
import exceptions.NoAccountWithSuchEmailException;
import repository.AccountRepository;
import repository.ContactInformationRepository;
import repository.NotificationRepository;
import repository.PersonalLinkRepository;
import response.AccountProfileInformations;

import org.springframework.http.ResponseEntity;
import java.util.List;

import org.aspectj.weaver.ast.Not;
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

    @GetMapping("/notification")
    public ResponseEntity<AccountProfileInformations> sendProfileInformations(@RequestParam String email) 
    {
        List<Notification> notification = notificationRepository.findByEmail(email);
        return null;
    }
}
