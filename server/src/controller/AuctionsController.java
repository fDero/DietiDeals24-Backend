package controller;

import entity.Auction;
import repository.AuctionRepository;
import repository.SilentAuctionRepository;
import repository.ReverseAuctionRepository;

import org.springframework.http.ResponseEntity;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/auctions/homepage")
    public ResponseEntity<String> sendProfileInformations(@RequestParam String email)  {
        List<Auction> auctions = auctionsRepository.findAll();
        return ResponseEntity.ok().body("OK");
    }
}
