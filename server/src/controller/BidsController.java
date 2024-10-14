package controller;

import entity.Bid;
import exceptions.AuctionNotActiveException;
import exceptions.BidOnYourOwnAuctionException;
import exceptions.MissingPaymentMethodException;
import exceptions.NoAuctionWithSuchIdException;
import exceptions.NoSuchAuctionException;
import exceptions.NoSuchPaymentMethodException;
import exceptions.PaymentMethodNotYoursException;
import request.NewBidRequest;
import response.BidsPack;
import service.AuctionManagementService;
import service.BidsManagementService;
import service.PaymentProcessingService;

import org.springframework.http.ResponseEntity;
import authentication.JwtTokenManager;

import authentication.RequireJWT;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Transactional
@RestController
public class BidsController {

    private final JwtTokenManager jwtTokenManager;
    private final BidsManagementService bidsManagementService;
    private final PaymentProcessingService paymentsService;

    @Autowired
    public BidsController(
        JwtTokenManager jwtTokenManager,
        AuctionManagementService auctionManagementService,
        BidsManagementService bidsManagementService,
        PaymentProcessingService paymentsService
    ){
        this.jwtTokenManager = jwtTokenManager;
        this.bidsManagementService = bidsManagementService;
        this.paymentsService = paymentsService;
    }

    @RequireJWT
    @PostMapping(value = "/bids/new", produces = "text/plain")
    public ResponseEntity<String> registerNewBid(
        @RequestHeader(name = "Authorization") String authorizationHeader,
        @RequestBody NewBidRequest newBidRequest
    ) 
        throws 
            NoSuchAuctionException,
            AuctionNotActiveException,
            BidOnYourOwnAuctionException, 
            NoAuctionWithSuchIdException, 
            NoSuchPaymentMethodException, 
            PaymentMethodNotYoursException, 
            MissingPaymentMethodException
    {        
        String jwtToken = jwtTokenManager.getTokenFromRequestHeader(authorizationHeader);
        String bidderIdStr = jwtTokenManager.getIdFromJWT(jwtToken);
        Integer bidderId = Integer.valueOf(bidderIdStr);
        Bid bid = new Bid();
        bid.setBidderId(bidderId);
        bid.setAuctionId(newBidRequest.getAuctionId());
        bid.setBidAmount(newBidRequest.getBidAmount());
        bid.setBidDate(new java.sql.Timestamp(System.currentTimeMillis()));
        bidsManagementService.saveBid(bid);
        String paymentRefoundToken = paymentsService.processBidPayment(newBidRequest, bidderId);
        bid.setPaymentInformations(paymentRefoundToken);
        return ResponseEntity.ok().body("done");
    }

    @RequireJWT
    @GetMapping(value = "/bids/own/by-auction", produces = "application/json")
    public ResponseEntity<BidsPack> sendOwnBidsByAuction(
        @RequestHeader(name = "Authorization") String authorizationHeader,
        @RequestParam Integer auctionId
    ) {
        String jwtToken = jwtTokenManager.getTokenFromRequestHeader(authorizationHeader);
        String userIdString = jwtTokenManager.getIdFromJWT(jwtToken);
        Integer userId = Integer.valueOf(userIdString);
        List<Bid> bids = bidsManagementService.fetchBidsByAuctionAndUser(auctionId, userId);
        BidsPack bidsPack = new BidsPack(bids);
        return ResponseEntity.ok().body(bidsPack);
    }
}