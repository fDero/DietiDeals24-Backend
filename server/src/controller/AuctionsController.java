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
    public ResponseEntity<AuctionsPack> sendAuctions(@RequestParam Integer page, @RequestParam Integer size)  {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        page = (page != null && page > 0)? page : 0;
        size = (size != null && size > 1)? size : 10;
        List<Auction> auctions = auctionsRepository.findAllByEndTimeAfterOrderByEndTimeAsc(now, PageRequest.of(page, size));
        AuctionsPack auctionsPack = new AuctionsPack(auctions);
        return ResponseEntity.ok().body(auctionsPack);
    }
}
