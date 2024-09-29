package service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import entity.Auction;
import repository.AuctionRepository;

@Service
public class AuctionFilteredSearchService {
    
    private final AuctionRepository auctionsRepository;

    @Autowired
    public AuctionFilteredSearchService(AuctionRepository auctionsRepository) {
        this.auctionsRepository = auctionsRepository;
    }

    public class AuctionFilter {

        private PageRequest page = PageRequest.of(0, 10);
        private String auctionType = "";
        private String macroCategory = "";
        private String searchString = "";
        private String itemCategory = "";
        private String policy = "";

        public AuctionFilter(Integer zeroIndexedPage, Integer size) {
            this.page = PageRequest.of(zeroIndexedPage, size);
        }

        public AuctionFilter searchingForAnAuctionOfType(String type) {
            this.auctionType = type;
            return this;
        }

        public AuctionFilter searchingForAnAuctionForItem(String macroCategory, String keywords, String category) {
            this.macroCategory = macroCategory;
            this.searchString = keywords;
            this.itemCategory = category;
            return this;
        }

        public AuctionFilter withPolicy(String policy) {
            if (!policy.equals("trending") && !policy.equals("expiration")) {
                throw new IllegalArgumentException("Invalid policy");
            }
            this.policy = policy;
            return this;
        }

        public List<Auction> fetchResults(){
            assert policy.equals("trending") || policy.equals("expiration");
            return (policy.equals("trending")) 
                ? auctionsRepository.findActiveAuctionsFilteredAndSortedByTrending(
                    itemCategory, auctionType, macroCategory, searchString, page
                ) 
                : auctionsRepository.findActiveAuctionsFilteredAndSortedByExpiration(
                    itemCategory, auctionType, macroCategory, searchString, page
                );
        }
    }

    public AuctionFilter performPagedSearch(Integer page, Integer size) {
        int zeroIndexedPage = page - 1;
        return new AuctionFilter(zeroIndexedPage, size);
    }
}
