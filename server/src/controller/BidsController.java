package controller;

import entity.Bid;
import exceptions.AuctionNotActiveException;
import exceptions.BidOnYourOwnAuctionException;
import exceptions.NoSuchAuctionException;
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

    private final JwtTokenManager jwtTokenProvider;
    private final AuctionManagementService auctionManagementService;
    private final BidsManagementService bidsManagementService;
    private final PaymentProcessingService paymentsService;

    @Autowired
    public BidsController(
        JwtTokenManager jwtTokenProvider,
        AuctionManagementService auctionManagementService,
        BidsManagementService bidsManagementService,
        PaymentProcessingService paymentsService
    ){
        this.jwtTokenProvider = jwtTokenProvider;
        this.bidsManagementService = bidsManagementService;
        this.auctionManagementService = auctionManagementService;
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
            BidOnYourOwnAuctionException
    {        
        auctionManagementService.updateStatuses();
        String jwtToken = jwtTokenProvider.getTokenFromRequestHeader(authorizationHeader);
        String bidderId = jwtTokenProvider.getIdFromJWT(jwtToken);
        Bid bid = new Bid();
        bid.setBidderId(Integer.valueOf(bidderId));
        bid.setAuctionId(newBidRequest.getAuctionId());
        bid.setBidAmount(newBidRequest.getBidAmount());
        bid.setBidDate(new java.sql.Timestamp(System.currentTimeMillis()));
        bidsManagementService.saveBid(bid);
        String paymentRefoundToken = paymentsService.doPayment(newBidRequest, bidderId);
        bid.setBidRefoundToken(paymentRefoundToken);
        return ResponseEntity.ok().body("done");
    }

    @RequireJWT
    @GetMapping(value = "/bids/own/by-auction", produces = "application/json")
    public ResponseEntity<BidsPack> sendOwnBidsByAuction(
        @RequestHeader(name = "Authorization") String authorizationHeader,
        @RequestParam Integer auctionId
    ) 
        throws 
            NoSuchAuctionException
    {        
        System.out.println("auctionId: " + auctionId);
        String jwtToken = jwtTokenProvider.getTokenFromRequestHeader(authorizationHeader);
        String userIdString = jwtTokenProvider.getIdFromJWT(jwtToken);
        Integer userId = Integer.valueOf(userIdString);
        List<Bid> bids = bidsManagementService.fetchBidsByAuctionAndUser(auctionId, userId);
        BidsPack bidsPack = new BidsPack(bids);
        System.out.println("bidsPack: " + bidsPack);
        return ResponseEntity.ok().body(bidsPack);
    }
}