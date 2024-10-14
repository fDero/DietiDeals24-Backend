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

    public class AuctionSearchFilter {

        private static final String TREANDING = "trending";
        private static final String EXPIRATION = "expiration";

        private PageRequest page = PageRequest.of(0, 10);
        private String auctionType = "";
        private String macroCategory = "";
        private String searchString = "";
        private String itemCategory = "";
        private String policy = TREANDING;

        public AuctionSearchFilter(Integer zeroIndexedPage, Integer size) {
            this.page = PageRequest.of(zeroIndexedPage, size);
        }

        public AuctionSearchFilter searchingForAnAuctionOfType(String type) {
            this.auctionType = type;
            return this;
        }

        public AuctionSearchFilter searchingForAnAuctionForItem(String macroCategory, String keywords, String category) {
            this.macroCategory = macroCategory;
            this.searchString = keywords;
            this.itemCategory = category;
            return this;
        }

        public AuctionSearchFilter withPolicy(String policy) {
            if (!TREANDING.equals(policy) && !EXPIRATION.equals(policy)) {
                throw new IllegalArgumentException("Invalid policy");
            }
            this.policy = policy;
            return this;
        }

        public List<Auction> fetchResults() {
            assert TREANDING.equals(policy) || EXPIRATION.equals(policy);
            return (TREANDING.equals(policy)) 
                ? auctionsRepository.findActiveAuctionsFilteredAndSortedByTrending(
                    itemCategory, auctionType, macroCategory, searchString, page
                ) 
                : auctionsRepository.findActiveAuctionsFilteredAndSortedByExpiration(
                    itemCategory, auctionType, macroCategory, searchString, page
                );
        }
    }

    public AuctionSearchFilter performPagedSearch(Integer page, Integer size) {
        int zeroIndexedPage = page - 1;
        return new AuctionSearchFilter(zeroIndexedPage, size);
    }
}
