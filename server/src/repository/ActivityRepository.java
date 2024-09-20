package repository;

import entity.Activity;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Integer> {

    @Query(
        value = 
            "SELECT * FROM get_user_activities(:userId, :includePastDeals, " +
            ":includeCurrentDeals, :includeAuctions, :includeBids) ", 
        nativeQuery = 
            true
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