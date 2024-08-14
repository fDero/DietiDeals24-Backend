package service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import repository.AuctionRepository;

@Service
public class AuctionManagementService {
    
    private final AuctionRepository auctionRepository;
    
    @Autowired
    public AuctionManagementService(
        AuctionRepository auctionRepository
    ) {
        this.auctionRepository = auctionRepository;
    }

    @Transactional
    public void updateAuctions() {
        auctionRepository.markAuctionsAsPending();
    }
}
