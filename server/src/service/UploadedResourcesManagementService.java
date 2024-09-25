
package service;

import java.net.URL;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import entity.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3;
import repository.ResourceRepository;

@Service
public class UploadedResourcesManagementService {
    
    private final AmazonS3 s3client;
    private final String bucketName;
    private final String region;
    private final String cloudfrontDomain;
    private final ResourceRepository resourceRepository;

    @Autowired
    public UploadedResourcesManagementService(
        @Value("${aws.s3.bucket-name}") String bucketName,
        @Value("${aws.s3.region}") String region,
        @Value("${aws.cloudfront.domain}") String cloudfrontDomain,
        AmazonS3 s3Service,
        ResourceRepository resourceRepository
    ) {
        this.s3client = s3Service;
        this.bucketName = bucketName;
        this.resourceRepository = resourceRepository;
        this.region = region;
        this.cloudfrontDomain = cloudfrontDomain;
    }

    private void saveResourceCreationRecord(URL url, String key) {
        final Timestamp currentTimestamp = Timestamp.from(Instant.now());
        final Resource resource = new Resource();
        resource.setResourceUrl(url.toString());
        resource.setUploadTimestamp(currentTimestamp);
        resource.setResourceKey(key);
        resourceRepository.save(resource);
    }

    public URL generatePresignedResourceUploadUrl(String fileExtension) {
        final Duration expiration = Duration.ofMinutes(10);
        final Date expirationDate = new Date(System.currentTimeMillis() + expiration.toMillis());
        final String key = Long.valueOf(System.currentTimeMillis()).toString() + "." + fileExtension;
        final GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, key)
            .withMethod(HttpMethod.PUT)
            .withExpiration(expirationDate);
        URL url = s3client.generatePresignedUrl(generatePresignedUrlRequest);
        saveResourceCreationRecord(url, key);
        return url;
    }

    @Transactional
    public String updateUrlAndKeepResource(String resourceUrl) {
        final Timestamp currentTimestamp = Timestamp.from(Instant.now());
        final String pattern = "/([^/?]+)(?:\\?|$)";
        final Pattern r = Pattern.compile(pattern);
        final Matcher m = r.matcher(resourceUrl);
        if (!m.find()) {
            throw new IllegalArgumentException("ill-formed url");
        }
        String key = m.group(1);
        Resource resource = resourceRepository.findResourceByResourceKey(key)
            .orElseThrow(() -> new IllegalArgumentException("invalid url"));
        resource.setConfirmationTimestamp(currentTimestamp);
        resourceRepository.save(resource);
        return cloudfrontDomain + "/" + key;
    }

    @Transactional
    public void updateUrlsAndKeepResources(String[] resourceUrls) {
        for (int i = 0; i < resourceUrls.length; i++) {
            resourceUrls[i] = updateUrlAndKeepResource(resourceUrls[i]);
        }
    }

    @Async
    @Transactional
    @Scheduled(fixedRate = 30*60000)
    public void removeObsoletePicturesFromDatabase() {
        final LocalDateTime threeHoursAgo = LocalDateTime.now().minusHours(3);
        final Timestamp expirationTime = Timestamp.valueOf(threeHoursAgo);
        final List<Resource> resources = resourceRepository.findObsoleteResources(expirationTime);
        System.out.println("Deleting obsolete resources...");
        boolean failure = false;
        for (Resource resource : resources) {
            try {
                s3client.deleteObject(bucketName, resource.getResourceKey());
            }
            catch (SdkClientException err) {
                failure = true;
            }
        }
        if (failure) {
            throw new RuntimeException("Error while deleting obsolete resources");
        }
        resourceRepository.deleteObsoleteResources(expirationTime);
    }
}
