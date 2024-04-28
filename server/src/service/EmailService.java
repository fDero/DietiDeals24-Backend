package service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mail_sender;

    @Autowired
    public EmailService(JavaMailSender mail_sender){
        this.mail_sender = mail_sender;
    }

    public void sendRegistrationConfirmEmail(String email, String auth_code){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Confirm your registration");
        message.setText("Your code is: " + auth_code);
        mail_sender.send(message);
    }
}
