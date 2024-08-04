package controller;

import org.springframework.http.ResponseEntity;
import authentication.JwtTokenManager;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import authentication.RequireJWT;

import entity.Bid;
import exceptions.AuctionNotActiveException;
import exceptions.BidOnYourOwnAuctionException;
import exceptions.NoSuchAuctionException;
import repository.BidRepository;
import service.BidValidationService;

@RestController
public class BidsController {

    private final JwtTokenManager jwtTokenProvider;
    private final BidRepository bidRepository;
    private final BidValidationService bidValidationService;

    @Autowired
    public BidsController(
        JwtTokenManager jwtTokenProvider,
        BidRepository bidRepository,
        BidValidationService bidValidationService
    ) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.bidRepository = bidRepository;
        this.bidValidationService = bidValidationService;
    }

    @RequireJWT
    @Transactional
    @GetMapping(value = "/bids/new", produces = "text/plain")
    public ResponseEntity<String> registerNewBid(
        @RequestHeader(name = "Authorization") String authorizationHeader,
        @RequestParam(name = "auctionId") Integer auctionId,
        @RequestParam(name = "amount") Long amount
    ) 
        throws 
            NoSuchAuctionException,
            AuctionNotActiveException,
            BidOnYourOwnAuctionException
    {
        String jwtToken = jwtTokenProvider.getTokenFromRequestHeader(authorizationHeader);
        String id = jwtTokenProvider.getIdFromJWT(jwtToken);
        Bid bid = new Bid();
        bid.setBidderId(Integer.valueOf(id));
        bid.setAuctionId(auctionId);
        bid.setBidAmount(amount);
        bid.setBidDate(new java.sql.Timestamp(System.currentTimeMillis()));
        bidRepository.save(bid);
        bidValidationService.validateBid(bid);
        return ResponseEntity.ok().body("done");
    }
}
