package edu.nyu.welcomehome.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DonationController {
    private static final Logger logger = LoggerFactory.getLogger(DonationController.class);


    @PostMapping("/donate")
    public void donate(String username, String password) {
        logger.info("Registering user " + username + " with password " + password);
    }
}
