package service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import entity.Account;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;


@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final String logoUrl;

    @Autowired
    public EmailService(
        JavaMailSender mailSender, 
        TemplateEngine templateEngine,
        @Value("${dietideals24.logo.url}") String logoUrl
    ){
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.logoUrl = logoUrl;
    }

    @Async
    public void sendRegistrationConfirmEmail(String email, String username, String authCode){
        System.out.println("Sending verification email to: " + email + " with code: " + authCode);
        HashMap<String, Object> templateModel = new HashMap<>();
        templateModel.put("username", username);
        templateModel.put("code", authCode);
        this.sendHtmlEmail(email, "Confirm your registration", "email-verification", templateModel);
    }

    @Async
    public void sendForgotPasswordEmail(Account account, String resetLink){
        System.out.println("Sending email to: " + account.getEmail() + " with resetLink: " + resetLink);
        HashMap<String, Object> templateModel = new HashMap<>();
        templateModel.put("username", account.getUsername());
        templateModel.put("resetLink", resetLink);
        this.sendHtmlEmail(account.getEmail(), "Reset your password", "reset-password", templateModel);
    }

    private void sendHtmlEmail(String to, String subject, String templateName, HashMap<String, Object> templateModel){
        try{
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
          
            helper.setTo(to);
            helper.setSubject(subject);

            templateModel.putIfAbsent("logoUrl", logoUrl);

            Context context = new Context();
            context.setVariables(templateModel);
            String htmlContent = templateEngine.process(templateName, context);

            helper.setText(htmlContent, true); 

            mailSender.send(message);
        } catch (MessagingException e){
            e.printStackTrace();
        }
    }
}
