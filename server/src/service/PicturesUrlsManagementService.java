
package service;

import java.net.URL;
import java.sql.Date;
import java.time.Duration;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3;

@Service
public class PicturesUrlsManagementService {
    
    private final AmazonS3 s3client;
    private final String bucketName;

    @Autowired
    public PicturesUrlsManagementService(
        @Value("${aws.s3.bucket-name}") String bucketName,
        AmazonS3 s3Service

    ) {
        this.s3client = s3Service;
        this.bucketName = bucketName;
    }

    public URL generatePresignedPictureUploadUrl(String fileExtension) {
        final Duration expiration = Duration.ofMinutes(10);
        final Date expirationDate = new Date(System.currentTimeMillis() + expiration.toMillis());
        final String key = Long.valueOf(System.currentTimeMillis()).toString() + "." + fileExtension;
        final GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, key)
            .withMethod(HttpMethod.PUT)
            .withExpiration(expirationDate);
        return s3client.generatePresignedUrl(generatePresignedUrlRequest);
    }
}
