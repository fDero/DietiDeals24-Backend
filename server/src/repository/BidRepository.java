
package repository;

import entity.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BidRepository extends JpaRepository<Bid, Integer> {

    @Query(
        value = 
            "SELECT * FROM get_user_bids(:id, :status)", 
        nativeQuery = 
            true
    )
    long countBidsByBidderIdAndAuctionStatus(Integer id, String status);

    default long countOnlineBidsByBidderId(Integer bidderId) {
        return countBidsByBidderIdAndAuctionStatus(bidderId, "active") + 
            countBidsByBidderIdAndAuctionStatus(bidderId, "pending");
    }

    default long countPastBidsByBidderId(Integer bidderId) {
        return countBidsByBidderIdAndAuctionStatus(bidderId, "closed");
    }
}