package edu.nyu.welcomehome.controllers;

import edu.nyu.welcomehome.models.request.LoginRequest;
import edu.nyu.welcomehome.models.response.ImmutableLoginResponse;
import edu.nyu.welcomehome.services.UserAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class UserAuthController {
    private static final Logger logger = LoggerFactory.getLogger(UserAuthController.class);

    private final UserAuthService userAuthService;

    @Autowired
    UserAuthController(UserAuthService userAuthService) {
        this.userAuthService = userAuthService;
    }

    @PostMapping("/register")
    public void register(String username, String password) {
        logger.info("Registering user " + username + " with password " + password);
    }

    @PostMapping(
            value = "/login",
            consumes =  MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableLoginResponse> login(
            @RequestBody LoginRequest request) {
        System.out.println(request.username());
        if (userAuthService.isAuthorizedUser(request.username(), request.password())) {
            ImmutableLoginResponse response = ImmutableLoginResponse.builder()
                    .status("success")
                    .message("User logged in successfully.")
                    .build();
            return ResponseEntity.ok(response);
        } else {
            ImmutableLoginResponse errorResponse = ImmutableLoginResponse.builder()
                    .status("failure")
                    .message("Invalid username or password")
                    .build();
            logger.info("Invalid username or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }
}
