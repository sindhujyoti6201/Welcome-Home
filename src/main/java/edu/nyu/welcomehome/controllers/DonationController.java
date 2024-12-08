package edu.nyu.welcomehome.controllers;

import edu.nyu.welcomehome.models.request.DonationRequest;
import edu.nyu.welcomehome.services.DonationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DonationController {

    @Autowired
    private DonationService donationService;

    @PostMapping("/donations")
    public ResponseEntity<String> submitDonation(@RequestBody DonationRequest donationData) {
        try {
            donationService.submitDonation(donationData);
            return new ResponseEntity<>("Donation recorded successfully!", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Donation recorded successfully!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
