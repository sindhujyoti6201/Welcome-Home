package edu.nyu.welcomehome.controllers;

import edu.nyu.welcomehome.models.request.LoginRequest;
import edu.nyu.welcomehome.models.request.RegisterRequest;
import edu.nyu.welcomehome.models.response.ImmutableLoginResponse;
import edu.nyu.welcomehome.models.response.ImmutableRegisterResponse;
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

    @PostMapping(value = "/volunteer/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableLoginResponse> volunteerLogin(@RequestBody LoginRequest request) {
        if (userAuthService.isAuthorizedAsStaff(request.username(), request.password())) {
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

    @PostMapping(value = "/customer/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableLoginResponse> customerLogin(@RequestBody LoginRequest request) {
        if (userAuthService.isAuthorizedAsCustomer(request.username(), request.password())) {
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

    @PostMapping(value = "/volunteer/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableRegisterResponse> volunteerRegister(@RequestBody RegisterRequest request) {
        try {
            logger.info(request.roleEnrolled().get(0).toString());
            userAuthService.saveUserAsVolunteer(request);

            ImmutableRegisterResponse response = ImmutableRegisterResponse.builder()
                    .status("success")
                    .message("Volunteer registered successfully!")
                    .build();

            return ResponseEntity.ok(response);
        } catch (Exception error) {
            ImmutableRegisterResponse errorResponse = ImmutableRegisterResponse.builder()
                    .status("failure")
                    .message("Unable to register the user. Try Again after some time!!")
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping(value = "/customer/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableRegisterResponse> customerRegister(@RequestBody RegisterRequest request) {
        try {
            userAuthService.saveUserAsCustomer(request);
            ImmutableRegisterResponse response = ImmutableRegisterResponse.builder()
                    .status("success")
                    .message("Customer registered successfully!")
                    .build();

            return ResponseEntity.ok(response);
        } catch (Exception error) {
            ImmutableRegisterResponse errorResponse = ImmutableRegisterResponse.builder()
                    .status("failure")
                    .message("Unable to register the user. Try Again after some time!!")
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}