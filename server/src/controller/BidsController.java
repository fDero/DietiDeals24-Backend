package controller;

import entity.Bid;
import exceptions.AuctionNotActiveException;
import exceptions.BidOnYourOwnAuctionException;
import exceptions.NoSuchAuctionException;
import request.NewBidRequest;
import service.AuctionManagementService;
import service.BidsManagementService;
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

    @Autowired
    public BidsController(
        JwtTokenManager jwtTokenProvider,
        AuctionManagementService auctionManagementService,
        BidsManagementService bidsManagementService
    ){
        this.jwtTokenProvider = jwtTokenProvider;
        this.bidsManagementService = bidsManagementService;
        this.auctionManagementService = auctionManagementService;
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
        System.out.println(newBidRequest.getAuctionId());
        System.out.println(newBidRequest.getBidAmount());
        String jwtToken = jwtTokenProvider.getTokenFromRequestHeader(authorizationHeader);
        String id = jwtTokenProvider.getIdFromJWT(jwtToken);
        Bid bid = new Bid();
        bid.setBidderId(Integer.valueOf(id));
        bid.setAuctionId(newBidRequest.getAuctionId());
        bid.setBidAmount(newBidRequest.getBidAmount());
        bid.setBidDate(new java.sql.Timestamp(System.currentTimeMillis()));
        bidsManagementService.saveBid(bid);
        return ResponseEntity.ok().body("done");
    }
}