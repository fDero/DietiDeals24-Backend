package controller;

import entity.Auction;
import exceptions.NoAuctionWithSuchIdException;
import repository.AuctionRepository;
import repository.SilentAuctionRepository;
import response.AuctionsPack;
import response.SpecificAuctionPublicInformations;
import repository.ReverseAuctionRepository;

import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

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

    @GetMapping(value = "/auctions/search", produces = "application/json")
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

    @GetMapping(value = "/auctions/specific/public-view", produces = "application/json")
    public ResponseEntity<SpecificAuctionPublicInformations> sendSpecificAuctionInformations(
        @RequestParam Integer id
    )  
        throws 
            NoAuctionWithSuchIdException
    {
        Optional<Auction> auction = auctionsRepository.findById(id);
        if (auction.isEmpty()) {
            throw new NoAuctionWithSuchIdException();
        }
        SpecificAuctionPublicInformations auctionSpecificInformations = new SpecificAuctionPublicInformations(auction.get());
        return ResponseEntity.ok().body(auctionSpecificInformations);
    }
}
