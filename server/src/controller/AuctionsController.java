package controller;

import entity.Auction;
import exceptions.NoAuctionWithSuchIdException;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import repository.AuctionRepository;
import request.NewAuctionRequest;
import response.AuctionsPack;
import response.SpecificAuctionPublicInformations;
import service.AuctionFilteredSearchService;
import service.AuctionManagementService;

import org.springframework.http.ResponseEntity;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import authentication.JwtTokenManager;
import authentication.RequireJWT;

@RestController
public class AuctionsController {
    
    private final AuctionRepository auctionsRepository;
    private final AuctionFilteredSearchService auctionFilteredSearchService;
    private final AuctionManagementService auctionManagementService;
    private final JwtTokenManager jwtTokenProvider;

    @Autowired
    public AuctionsController(
        AuctionRepository auctionsRepository,
        AuctionFilteredSearchService auctionFilteredSearchService,
        AuctionManagementService auctionManagementService,
        JwtTokenManager jwtTokenProvider
    ) {
        auctionManagementService.updateStatuses();
        this.auctionsRepository = auctionsRepository;
        this.auctionFilteredSearchService = auctionFilteredSearchService;
        this.auctionManagementService = auctionManagementService;
        this.jwtTokenProvider = jwtTokenProvider;
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
        @RequestParam Integer id
    )  
        throws 
            NoAuctionWithSuchIdException
    {
        auctionManagementService.updateStatuses();
        Auction auction = auctionsRepository.findById(id).orElseThrow(() -> new NoAuctionWithSuchIdException());
        SpecificAuctionPublicInformations auctionSpecificInformations = new SpecificAuctionPublicInformations(auction);
        return ResponseEntity.ok().body(auctionSpecificInformations);
    }

    @RequireJWT
    @PostMapping(value = "/auctions/new", produces = "text/plain")
    public ResponseEntity<String> createNewAuction(
        @RequestHeader(name = "Authorization") String authorizationHeader,
        @RequestBody NewAuctionRequest newAuctionRequest
    )  
    {
        String jwtToken = jwtTokenProvider.getTokenFromRequestHeader(authorizationHeader);
        Integer creatorId = Integer.valueOf(jwtTokenProvider.getIdFromJWT(jwtToken));
        auctionManagementService.createNewAuction(creatorId, newAuctionRequest);
        return ResponseEntity.ok().body("done");
    }
}
