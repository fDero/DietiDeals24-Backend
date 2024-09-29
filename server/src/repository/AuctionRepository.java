package repository;

import entity.Auction;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import java.sql.Timestamp;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, Integer> {

    Optional<Auction> findById(Integer id);

    List<Auction> findAllByCreatorIdAndStatus(Integer creatorId, String status);

    List<Auction> findAllByEndTimeAfterOrderByEndTimeAsc(Timestamp currentTime, Pageable pageable);

    final static String auctionSearchQuery =
        "SELECT a FROM Auction a WHERE a.status = 'active' AND a.endTime > CURRENT_TIMESTAMP " +
        "AND (:itemCategory IS NULL OR :itemCategory = '' OR a.itemCategory = :itemCategory) " +
        "AND (:auctionType IS NULL OR :auctionType = '' OR LOWER(a.auctionType) = LOWER(:auctionType)) " +
        "AND (:macroCategory IS NULL OR :macroCategory = '' OR a.macroCategory = :macroCategory) " +
        "AND (:searchString IS NULL OR :searchString = '' OR " +
        "     UPPER(a.itemName) LIKE CONCAT('%', UPPER(:searchString), '%') OR " +
        "     UPPER(a.description) LIKE CONCAT('%', UPPER(:searchString), '%')) " +
        "ORDER BY CASE WHEN UPPER(a.itemName) LIKE CONCAT('%', UPPER(:searchString), '%') THEN 0 ELSE 1 END "; 

    @Query(auctionSearchQuery + " , a.endTime ASC")
    List<Auction> findActiveAuctionsFilteredAndSortedByExpiration(
        String itemCategory,
        String auctionType,
        String macroCategory,
        String searchString,
        Pageable pageable
    );

    
    @Query(auctionSearchQuery + " , a.numberOfBids DESC")
    List<Auction> findActiveAuctionsFilteredAndSortedByTrending(
        String itemCategory,
        String auctionType,
        String macroCategory,
        String searchString,
        Pageable pageable
    );

    long countByStatusAndCreatorId(String status, int creatorId);

    long countByStatusAndCurrentBidderId(String status, int currentBidderId);

    default long countPastAuctionsByCreatorId(Integer id) {
        return countByStatusAndCreatorId("closed", id)
            + countByStatusAndCreatorId("rejected", id);
    }

    default long countOnlineAuctionsByCreatorId(Integer id) {
        return countByStatusAndCreatorId("active", id) + 
            countByStatusAndCreatorId("pending", id);
    }

    @Modifying
    @Query("UPDATE Auction a SET a.status = 'pending' WHERE a.status = 'active' AND a.endTime < CURRENT_TIMESTAMP")
    void markAuctionsAsPending();

    @Modifying
    @Query("UPDATE Auction a SET a.status = 'aborted' WHERE a.status = 'pending' AND a.currentBidderId is NULL")
    void markAuctionsAsAbortedForMissingBids();

    @Modifying
    @Query("UPDATE Auction a SET a.status = 'rejected' WHERE a.status = 'pending' AND a.endTime < :expirationTime")
    void markAuctionsAsRejectedForExpiration(Timestamp expirationTime);
}
