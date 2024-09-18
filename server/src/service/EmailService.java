package service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import entity.Account;
import entity.Auction;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;


@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final String logoUrl;
    private final String frontendUrl;
    private final String dietideals24Email;

    @Autowired
    public EmailService(
        JavaMailSender mailSender, 
        TemplateEngine templateEngine,
        @Value("${dietideals24.logo.url}") String logoUrl,
        @Value("${dietideals24.frontend.url}") String frontendUrl,
        @Value("${dietideals24.email}") String dietideals24Email
    ) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.logoUrl = logoUrl;
        this.frontendUrl = frontendUrl;
        this.dietideals24Email = dietideals24Email;
    }

    @Async
    public void sendRegistrationConfirmEmail(String email, String username, String authCode) 
        throws 
            MessagingException 
    {
        System.out.println("Sending verification email to: " + email + " with code: " + authCode);
        HashMap<String, Object> templateModel = new HashMap<>();
        templateModel.put("username", username);
        templateModel.put("code", authCode);
        this.sendHtmlEmail(email, "Confirm your registration", "email-verification", templateModel);
    }

    @Async
    public void sendForgotPasswordEmail(Account account, String authToken) 
        throws 
            UnsupportedEncodingException, 
            MessagingException 
    {
        String encodedAccountId = URLEncoder.encode(account.getId().toString(), StandardCharsets.UTF_8.toString());
        String encodedAuthToken = URLEncoder.encode(authToken, StandardCharsets.UTF_8.toString());
        String resetLink = frontendUrl + "/reset-password/" + encodedAccountId + "/" + encodedAuthToken;
        System.out.println("Sending email to: " + account.getEmail() + " with resetLink: " + resetLink);
        HashMap<String, Object> templateModel = new HashMap<>();
        templateModel.put("username", account.getUsername());
        templateModel.put("resetLink", resetLink);
        this.sendHtmlEmail(account.getEmail(), "Reset your password", "reset-password", templateModel);
    }

    @Async
    public void sendUserMessageEmail(Auction auction, String message, Account sender, Account receiver) 
        throws 
            UnsupportedEncodingException, 
            MessagingException 
    {
        String encodedAuctionId = URLEncoder.encode(auction.getId().toString(), StandardCharsets.UTF_8.toString());
        String respondUrl = frontendUrl + "/message/" + encodedAuctionId;
        HashMap<String, Object> templateModel = new HashMap<>();
        templateModel.put("sender", sender.getUsername());
        templateModel.put("receiver", receiver.getUsername());
        templateModel.put("auctionTitle", auction.getItemName());
        templateModel.put("message", message);
        templateModel.put("respondUrl", respondUrl);
        this.sendHtmlEmail(receiver.getEmail(), "You have a new message", "user-message", templateModel);
    }

    @Async
    public void sendReportEmail(Auction auction, String message, Account reporter, Account reportee) 
        throws 
            MessagingException, 
            UnsupportedEncodingException 
    {
        String encodedAuctionId = URLEncoder.encode(auction.getId().toString(), StandardCharsets.UTF_8.toString());
        String respondUrl = frontendUrl + "/message/" + encodedAuctionId;
        HashMap<String, Object> templateModel = new HashMap<>();
        templateModel.put("sender", reporter.getUsername());
        templateModel.put("receiver", reportee.getUsername());
        templateModel.put("auctionTitle", auction.getItemName());
        templateModel.put("message", message);
        templateModel.put("respondUrl", respondUrl);
        this.sendHtmlEmail(dietideals24Email, "An user has been reported", "user-report", templateModel);
    }

    private void sendHtmlEmail(
        String receiver, 
        String subject, 
        String template, 
        HashMap<String, Object> templateModel
    ) 
        throws 
            MessagingException 
    {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(receiver);
        helper.setSubject(subject);
        templateModel.putIfAbsent("logoUrl", logoUrl);
        Context context = new Context();
        context.setVariables(templateModel);
        String htmlContent = templateEngine.process(template, context);
        helper.setText(htmlContent, true); 
        mailSender.send(message);
    }
}
