package controller;

import entity.Notification;
import exceptions.NoAccountWithSuchEmailException;
import repository.NotificationRepository;
import response.NotificationsPack;

import org.springframework.http.ResponseEntity;
import java.util.List;
import authentication.JwtTokenProvider;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import authentication.RequireJWT;


@RestController
public class NotificationController {
    
    private final NotificationRepository notificationRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public NotificationController(
        NotificationRepository notificationRepository,
        JwtTokenProvider jwtTokenProvider
    ) {
        this.notificationRepository = notificationRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @RequireJWT
    @GetMapping("/notifications")
    public ResponseEntity<NotificationsPack> sendProfileInformations(@RequestHeader(name = "Authorization") String authorizationHeader) {
        String jwtToken = jwtTokenProvider.getTokenFromRequestHeader(authorizationHeader);
        String email = jwtTokenProvider.getEmailFromJWT(jwtToken);
        List<Notification> notifications = notificationRepository.findByEmail(email);
        return ResponseEntity.ok().body(new NotificationsPack(notifications));
    }
}
