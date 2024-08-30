package controller;

import entity.Auction;
import exceptions.NoAuctionWithSuchIdException;
import repository.AuctionRepository;
import response.AuctionsPack;
import response.SpecificAuctionPublicInformations;
import service.AuctionFilteredSearchService;
import service.AuctionManagementService;

import org.springframework.http.ResponseEntity;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuctionsController {
    
    private final AuctionRepository auctionsRepository;
    private final AuctionFilteredSearchService auctionFilteredSearchService;
    private final AuctionManagementService auctionManagementService;

    @Autowired
    public AuctionsController(
        AuctionRepository auctionsRepository,
        AuctionFilteredSearchService auctionFilteredSearchService,
        AuctionManagementService auctionManagementService
    ) {
        auctionManagementService.updateStatuses();
        this.auctionsRepository = auctionsRepository;
        this.auctionFilteredSearchService = auctionFilteredSearchService;
        this.auctionManagementService = auctionManagementService;
    }

    @GetMapping(value = "/auctions/search", produces = "application/json")
    public ResponseEntity<AuctionsPack> sendAuctionSearchOutcome(
        @RequestParam(defaultValue = "1")          Integer page, 
        @RequestParam(defaultValue = "10")         Integer size,
        @RequestParam(defaultValue = "")           String macroCategory,
        @RequestParam(defaultValue = "")           String keywords,
        @RequestParam(defaultValue = "")           String category,
        @RequestParam(defaultValue = "")           String type,
        @RequestParam(defaultValue = "trending")   String policy
    )  {
        auctionManagementService.updateStatuses();
        List<Auction> auctions = auctionFilteredSearchService
            .performPagedSearch(page, size)
            .searchingForAnAuctionOfType(type)
            .searchingForAnAuctionForItem(macroCategory, keywords, category)
            .withPolicy(policy)
            .fetchResults();
        AuctionsPack auctionsPack = new AuctionsPack(auctions);
        return ResponseEntity.ok().body(auctionsPack);
    }

    @GetMapping(value = "/auctions/specific/public-view", produces = "application/json")
    public ResponseEntity<SpecificAuctionPublicInformations> sendSpecificAuctionInformations(
        @RequestParam Integer id,
        @RequestHeader String Authorization
    )  
        throws 
            NoAuctionWithSuchIdException
    {
        auctionManagementService.updateStatuses();
        Auction auction = auctionsRepository.findById(id).orElseThrow(() -> new NoAuctionWithSuchIdException());
        SpecificAuctionPublicInformations auctionSpecificInformations = new SpecificAuctionPublicInformations(auction);
        return ResponseEntity.ok().body(auctionSpecificInformations);
    }
}
