package repository;

import entity.Category;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    
    List<Category> findAll();

    @Query(
        value = "SELECT * FROM get_trending_categories() LIMIT :amount", 
        nativeQuery = true
    ) 
    List<Category> findTreanding(Integer amount);
}