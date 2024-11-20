package edu.nyu.welcomehome.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class UserAuthController {
    private static final Logger logger = LoggerFactory.getLogger(UserAuthController.class);


    @PostMapping("/register")
    public void register(String username, String password) {
        logger.info("Registering user " + username + " with password " + password);
    }

    @PostMapping("/login")
    public void login(String username, String password) {
        logger.info("Logging in user " + username + " with password " + password);
    }

}
