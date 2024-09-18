package controller;

import entity.Account;
import entity.Auction;
import exceptions.NoAccountWithSuchIdException;
import exceptions.NoSuchAuctionException;
import jakarta.mail.MessagingException;
import request.MessageForwardRequest;
import service.AccountManagementService;
import service.AuctionManagementService;
import service.EmailService;
import org.springframework.http.ResponseEntity;
import java.io.UnsupportedEncodingException;
import authentication.JwtTokenManager;

import authentication.RequireJWT;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@Transactional
@RestController
public class MessageController {

    private final EmailService emailService;
    private final AuctionManagementService auctionManagementService;
    private final AccountManagementService accountManagementService;
    private final JwtTokenManager jwtTokenProvider;

    @Autowired
    public MessageController(
        EmailService emailService,
        AccountManagementService accountManagementService,
        AuctionManagementService auctionManagementService,
        JwtTokenManager jwtTokenProvider
    ){
        this.emailService = emailService;
        this.accountManagementService = accountManagementService;
        this.auctionManagementService = auctionManagementService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @RequireJWT
    @PostMapping(value = "/message/forward/email", produces = "text/plain")
    public ResponseEntity<String> forwardEmailMessage(
        @RequestHeader(name = "Authorization") String  authorizationHeader,
        @RequestBody MessageForwardRequest request
    ) 
        throws 
            NoSuchAuctionException, 
            NoAccountWithSuchIdException, 
            UnsupportedEncodingException, 
            MessagingException 
    {
        final Auction auction = auctionManagementService.findById(request.getAuctionId());
        final String  jwtToken = jwtTokenProvider.getTokenFromRequestHeader(authorizationHeader);
        final String  requesterIdStr = jwtTokenProvider.getIdFromJWT(jwtToken);
        final Integer senderId = Integer.valueOf(requesterIdStr);
        final Integer currentBidderId = auction.getCurrentBidderId().intValue();
        final Integer receiverId = currentBidderId.equals(senderId) ? auction.getCreatorId() : currentBidderId;
        final Account receiver = accountManagementService.fetchAccountById(receiverId);
        final Account sender = accountManagementService.fetchAccountById(senderId);
        emailService.sendUserMessageEmail(auction, request.getMessage(), sender, receiver);
        return ResponseEntity.ok().body("done");
    }
}