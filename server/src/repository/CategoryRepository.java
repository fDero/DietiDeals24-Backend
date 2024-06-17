package repository;

import entity.Category;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    
    /* the implementation of this interface gets auto-generated by Hibernate          */
    /* to create a repository, use the spring-boot injection techniques to generate   */
    /* an object of type 'Category' such as using @Autowired annotations              */
    /* on class internal fields and use them as if they were implemented. Hibernate   */
    /* will generate the code for you and database access functionalities will be     */
    /* provided to you. Please read the spring-boot wiki to understand better         */

    List<Category> findAll();
}