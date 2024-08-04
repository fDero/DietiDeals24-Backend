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

        private Integer zeroIndexedPage = 0;
        private Integer size = 10;
        private String type = "";
        private String macroCategory = "";
        private String keywords = "";
        private String category = "";
        private String policy = "";

        public AuctionFilter(Integer zeroIndexedPage, Integer size) {
            this.zeroIndexedPage = zeroIndexedPage;
            this.size = size;
        }

        public AuctionFilter searchingForAnAuctionOfType(String type) {
            this.type = type;
            return this;
        }

        public AuctionFilter searchingForAnAuctionForItem(String macroCategory, String keywords, String category) {
            this.macroCategory = macroCategory;
            this.keywords = keywords;
            this.category = category;
            return this;
        }

        public AuctionFilter withPolicy(String policy) {
            this.policy = policy;
            return this;
        }

        public List<Auction> fetchResults(){
            if (policy.equals("expiration")) {
                return auctionsRepository.findActiveAuctionsFilteredExpiration(
                    category, 
                    keywords, 
                    macroCategory, 
                    type,
                    PageRequest.of(zeroIndexedPage, size)
                );
            }
            else if (policy.equals("trending")) {
                return auctionsRepository.findActiveAuctionsFilteredTrending(
                    category, 
                    keywords, 
                    macroCategory, 
                    type,
                    PageRequest.of(zeroIndexedPage, size)
                );
            }
            else { 
                throw new IllegalArgumentException("Invalid policy");
            }
        }
    }

    public AuctionFilter performPagedSearch(Integer page, Integer size) {
        int zeroIndexedPage = page - 1;
        return new AuctionFilter(zeroIndexedPage, size);
    }
}
