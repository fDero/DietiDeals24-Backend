package controller;

import entity.Auction;
import repository.AuctionRepository;
import repository.SilentAuctionRepository;
import response.AuctionsPack;
import repository.ReverseAuctionRepository;

import org.springframework.http.ResponseEntity;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuctionsController {
    
    private final AuctionRepository auctionsRepository;
    private final SilentAuctionRepository silentAuctionsRepository;
    private final ReverseAuctionRepository reverseAuctionsRepository;

    @Autowired
    public AuctionsController(
        AuctionRepository auctionsRepository,
        SilentAuctionRepository silentAuctionsRepository,
        ReverseAuctionRepository reverseAuctionsRepository
    ) {
        this.auctionsRepository = auctionsRepository;
        this.silentAuctionsRepository = silentAuctionsRepository;
        this.reverseAuctionsRepository = reverseAuctionsRepository;
    }

    @GetMapping("/auctions/search")
    public ResponseEntity<AuctionsPack> sendAuctionSearchOutcome(
        @RequestParam(defaultValue = "1")          Integer page, 
        @RequestParam(defaultValue = "10")         Integer size,
        @RequestParam(defaultValue = "")           String macroCategory,
        @RequestParam(defaultValue = "")           String keywords,
        @RequestParam(defaultValue = "")           String category,
        @RequestParam(defaultValue = "")           String type,
        @RequestParam(defaultValue = "expiration") String policy
    )  {
        int zeroIndexedPage = page - 1;
        List<Auction> auctions = null;
        if (policy.equals("expiration"))
            auctions = auctionsRepository.findActiveAuctionsFilteredExpiration(
                category, 
                keywords, 
                macroCategory, 
                type,
                PageRequest.of(zeroIndexedPage, size)
            );
        else if (policy.equals("trending"))
            auctions = auctionsRepository.findActiveAuctionsFilteredTrending(
                category, 
                keywords, 
                macroCategory, 
                type,
                PageRequest.of(zeroIndexedPage, size)
            );
        else throw new IllegalArgumentException("Invalid policy");
        AuctionsPack auctionsPack = new AuctionsPack(auctions);
        return ResponseEntity.ok().body(auctionsPack);
    }
}
