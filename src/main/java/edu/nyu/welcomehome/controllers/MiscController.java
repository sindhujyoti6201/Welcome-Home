package edu.nyu.welcomehome.controllers;

import edu.nyu.welcomehome.services.MiscService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MiscController {
    private static final Logger logger = LoggerFactory.getLogger(MiscController.class);

    private final MiscService miscService;

    @Autowired
    MiscController(MiscService miscService) {
        this.miscService = miscService;
    }

    @GetMapping(value = "/images/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getImage(@PathVariable int id) {
        byte[] imageData = miscService.getImageById(id);
        System.out.println("Image data size: " + (imageData != null ? imageData.length : 0));

        if (imageData == null || imageData.length == 0) {
            // If no data is found, return 404 not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "image/jpeg"); // Set appropriate MIME type
        headers.set("Content-Disposition", "inline; filename=\"image.jpg\"");
        return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
    }

}
