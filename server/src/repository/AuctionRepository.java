package repository;

import entity.Auction;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;
import java.sql.Timestamp;

import org.springframework.stereotype.Repository;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, Integer> {

        Optional<Auction> findById(Integer id);

        List<Auction> findAllByCreatorIdAndStatus(Integer creatorId, String status);

        List<Auction> findAllByEndTimeAfterOrderByEndTimeAsc(Timestamp currentTime, Pageable pageable);

        List<Auction> findAllByEndTimeAfterAndItemCategoryContainingAndItemNameContainingAndMacroCategoryContainingAndAuctionTypeContainingOrderByEndTimeAsc(
                Timestamp currentTime, String itemCategory, String itemName, 
                String auctionType, String macroCategory, Pageable pageable
        );

        List<Auction> findAllByEndTimeAfterAndItemCategoryContainingAndItemNameContainingAndMacroCategoryContainingAndAuctionTypeContainingOrderByNumberOfBidsDesc(
                Timestamp currentTime, String itemCategory, String itemName, 
                String auctionType, String macroCategory, Pageable pageable
        );

        default List<Auction> findActiveAuctionsFilteredExpiration(
                String itemCategory, String searchString, 
                String auctionType, String macroCategory, 
                Pageable pageable
        ) {
                return findAllByEndTimeAfterAndItemCategoryContainingAndItemNameContainingAndMacroCategoryContainingAndAuctionTypeContainingOrderByEndTimeAsc(
                        new Timestamp(System.currentTimeMillis()), itemCategory, searchString, auctionType, macroCategory, pageable
                );
        };

        default List<Auction> findActiveAuctionsFilteredTrending(
                String itemCategory, String searchString, 
                String auctionType, String macroCategory, 
                Pageable pageable
        ) {
                return findAllByEndTimeAfterAndItemCategoryContainingAndItemNameContainingAndMacroCategoryContainingAndAuctionTypeContainingOrderByNumberOfBidsDesc(
                        new Timestamp(System.currentTimeMillis()), itemCategory, searchString, auctionType, macroCategory, pageable
                );
        };

        long countByStatusAndCreatorId(String status, int creatorId);

        @Query("SELECT COUNT(a) FROM Auction a WHERE a.status = 'closed' AND a.currentBidderId = ?1")
        long countByWinnerId(int winnerId);

        default long countPastDealsById(Integer id){
                return countByStatusAndCreatorId("closed", id) + countByWinnerId(id);
        }

        default long countOnlineAuctionsById(Integer id){
                return countByStatusAndCreatorId("active", id);
        }

        @Modifying
        @Query("UPDATE Auction a SET a.status = 'pending' WHERE a.status = 'active' AND a.endTime < CURRENT_TIMESTAMP")
        void markAuctionsAsPending();
}
