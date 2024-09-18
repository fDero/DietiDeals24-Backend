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

import org.springframework.http.HttpStatus;
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
        if (!senderId.equals(auction.getCreatorId()) && !senderId.equals(currentBidderId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You cannot send a message for this auction");
        }
        if (!auction.getStatus().equals("closed")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Auction is not closed yet");
        }
        emailService.sendUserMessageEmail(auction, request.getMessage(), sender, receiver);
        return ResponseEntity.ok().body("done");
    }

    @RequireJWT
    @PostMapping(value = "/message/report/email", produces = "text/plain")
    public ResponseEntity<String> forwardReportEmail(
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
        final Integer reporterId = Integer.valueOf(requesterIdStr);
        final Integer currentBidderId = auction.getCurrentBidderId().intValue();
        final Integer reporteeId = currentBidderId.equals(reporterId) ? auction.getCreatorId() : currentBidderId;
        final Account reportee = accountManagementService.fetchAccountById(reporteeId);
        final Account reporter = accountManagementService.fetchAccountById(reporterId);
        if (!reporterId.equals(auction.getCreatorId()) && !reporterId.equals(currentBidderId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You cannot report an user for this auction");
        }
        if (!auction.getStatus().equals("closed")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Auction is not closed yet");
        }
        emailService.sendReportEmail(auction, request.getMessage(), reporter, reportee);
        return ResponseEntity.ok().body("done");
    }
}