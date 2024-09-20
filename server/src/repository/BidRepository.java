
package repository;

import entity.Bid;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BidRepository extends JpaRepository<Bid, Integer> {

    @Query(
        value = "SELECT * FROM get_user_bids(:bidderId, :auctionStatus)", 
        nativeQuery = true
    )
    long countBidsByBidderIdAndAuctionStatus(Integer bidderId, String auctionStatus);

    List<Bid> findByBidderIdAndAuctionId(Integer bidderId, Integer auctionId);

    default long countOnlineBidsByBidderId(Integer bidderId) {
        return countBidsByBidderIdAndAuctionStatus(bidderId, "active") + 
            countBidsByBidderIdAndAuctionStatus(bidderId, "pending");
    }

    default long countPastBidsByBidderId(Integer bidderId) {
        return countBidsByBidderIdAndAuctionStatus(bidderId, "closed") +
            countBidsByBidderIdAndAuctionStatus(bidderId, "rejected");
    }
}