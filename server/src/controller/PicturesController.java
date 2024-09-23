package controller;

import exceptions.InvalidPictureFileExtension;
import org.springframework.web.bind.annotation.RequestParam;
import response.URLResponse;
import service.PicturesUrlsManagementService;
import org.springframework.http.ResponseEntity;

import java.net.URL;
import authentication.JwtTokenManager;

import authentication.RequireJWT;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@Transactional
@RestController
public class PicturesController {

    private final PicturesUrlsManagementService picturesUrlsManagementService;
    private final JwtTokenManager jwtTokenManager;

    @Autowired
    public PicturesController(
        PicturesUrlsManagementService picturesUrlsManagementService,
        JwtTokenManager jwtTokenManager
    ) {
        this.picturesUrlsManagementService = picturesUrlsManagementService;
        this.jwtTokenManager = jwtTokenManager;
    }

    @RequireJWT
    @GetMapping(value = "/files/upload/picture/pre-signed-upload-url", produces = "application/json")
    public ResponseEntity<URLResponse> sendPreSignedUploadPictureUrl(
        @RequestHeader(name = "Authorization") String authorizationHeader,
        @RequestParam String fileExtension
    )
        throws
            InvalidPictureFileExtension
    {
        if (!fileExtension.equals("png") && !fileExtension.equals("jpeg") && !fileExtension.equals("webp")) {
            throw new InvalidPictureFileExtension();
        }
        URL url = picturesUrlsManagementService.generatePresignedPictureUploadUrl(fileExtension);
        URLResponse urlResponse = new URLResponse(url);
        return ResponseEntity.ok().body(urlResponse);
    }
}