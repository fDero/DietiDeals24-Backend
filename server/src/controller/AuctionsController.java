package controller;

import entity.Auction;
import repository.AuctionRepository;
import repository.SilentAuctionRepository;
import response.AuctionsPack;
import repository.ReverseAuctionRepository;

import org.springframework.http.ResponseEntity;

import java.sql.Timestamp;
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

    @GetMapping("/auctions")
    public ResponseEntity<AuctionsPack> sendAuctions(@RequestParam Integer page)  {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        List<Auction> auctions = auctionsRepository.findAllByEndTimeAfterOrderByEndTimeAsc(now, PageRequest.of(page, 10));
        AuctionsPack auctionsPack = new AuctionsPack(auctions);
        return ResponseEntity.ok().body(auctionsPack);
    }
}
