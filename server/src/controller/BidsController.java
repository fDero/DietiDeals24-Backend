package controller;

import entity.Bid;
import exceptions.AuctionNotActiveException;
import exceptions.BidOnYourOwnAuctionException;
import exceptions.NoSuchAuctionException;
import request.NewBidRequest;
import service.AuctionManagementService;
import service.BidsManagementService;
import service.PaymentProcessingService;

import org.springframework.http.ResponseEntity;
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
}