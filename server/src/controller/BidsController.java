package controller;

import org.springframework.http.ResponseEntity;
import authentication.JwtTokenManager;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import authentication.RequireJWT;

import entity.Bid;
import exceptions.AuctionNotActiveException;
import exceptions.BidOnYourOwnAuctionException;
import exceptions.NoSuchAuctionException;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import request.NewBidRequest;
import service.AuctionManagementService;
import service.BidsManagementService;

@RestController
public class BidsController {

    private final JwtTokenManager jwtTokenProvider;
    private final BidsManagementService bidsManagementService;
    private final AuctionManagementService auctionManagementService;

    @Autowired
    public BidsController(
        JwtTokenManager jwtTokenProvider,
        BidsManagementService bidsManagementService,
        AuctionManagementService auctionManagementService
    ) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.bidsManagementService = bidsManagementService;
        this.auctionManagementService = auctionManagementService;
    }

    @RequireJWT
    @Transactional
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
        auctionManagementService.updateAuctions();
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
