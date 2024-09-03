package repository;

import entity.Activity;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Integer> {

    static String chronologicalCheck = 
        " ((:includeCurrentDeals = true AND (a.status = 'active' OR a.status = 'pending')) " +
        " OR (:includePastDeals = true AND a.status != 'aborted'))";

    static String domainCheck = 
        "((:includeAuctions = true AND a.creatorId = :userId) " +
        " OR (:includeBids = true AND a.currentBidderId = :userId))";

    static String currentSilentAuctionCheck =
        "(:includeBids = true AND a.bidderId = :userId" + 
        " AND (a.status = 'active' OR a.status = 'pending'))"; 

    @Query(
        "SELECT a FROM Activity a  WHERE " + chronologicalCheck + " AND " + 
        "(" + domainCheck + " OR " + currentSilentAuctionCheck + ") "
    )
    List<Activity> findUserActivityByUserById(
        Integer userId, 
        boolean includePastDeals, 
        boolean includeCurrentDeals, 
        boolean includeAuctions,
        boolean includeBids,
        Pageable pageable
    );
}