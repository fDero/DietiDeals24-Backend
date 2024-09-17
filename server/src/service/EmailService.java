package service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import entity.Account;


@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Autowired
    public EmailService(
        JavaMailSender mailSender, 
        @Value("${dietideals24.frontend.url}") String frontendUrl
    ){
        this.mailSender = mailSender;
    }

    @Async
    public void sendRegistrationConfirmEmail(String email, String authCode){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Confirm your registration");
        message.setText("Your code is: " + authCode);
        System.out.println("Sending email to: " + email + " with code: " + authCode);
        mailSender.send(message);
    }

    @Async
    public void sendForgotPasswordEmail(Account account, String resetLink){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(account.getEmail());
        message.setSubject("Forgot your password?");
        message.setText("Follow this link to reset your password " + resetLink + "\n\nIf you didn't request this, please ignore this email.");
        System.out.println("Sending email to: " + account.getEmail() + " with resetLink: " + resetLink);
        mailSender.send(message);
    }
}
