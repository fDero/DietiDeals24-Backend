package spring;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.benmanes.caffeine.cache.Caffeine;

@Configuration
@EnableCaching
public class CacheConfig {

  @Bean
  public Caffeine caffeineConfig() {
    return Caffeine.newBuilder();
  }

  @Bean
  public CaffeineCacheManager cacheManager() {
    CaffeineCacheManager cacheManager = new CaffeineCacheManager("countries", "cities");
    cacheManager.setCaffeine(Caffeine.newBuilder()/* .maximumSize(16) */);

    return cacheManager;
  }
}
