package repository;

import entity.OAuthAccountBinding;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;


import org.springframework.stereotype.Repository;

@Repository
public interface OAuthAccountBindingRepository extends JpaRepository<OAuthAccountBinding, Integer> {
    
    Optional<OAuthAccountBinding> findByOauthAccountIdAndOauthProvider(
        String oauthAccountId, 
        String oauthProvider
    );
}
