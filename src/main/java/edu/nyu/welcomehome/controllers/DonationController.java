package edu.nyu.welcomehome.controllers;

import edu.nyu.welcomehome.models.request.LoginRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DonationController {
    private static final Logger logger = LoggerFactory.getLogger(DonationController.class);

    @PostMapping("/donate")
    public void donate(@RequestBody LoginRequest request) {
        logger.info("Registering user " + request.username() + " with password " + request.password());
    }
}
