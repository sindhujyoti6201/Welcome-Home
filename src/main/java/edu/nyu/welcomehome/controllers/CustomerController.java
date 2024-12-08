package edu.nyu.welcomehome.controllers;

import edu.nyu.welcomehome.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    private final ItemService itemService;

    @Autowired
    public CustomerController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/{username}")
    public ResponseEntity<?> getCustomerData(@PathVariable String username) {
        // Assuming `getCustomerData` is a method in your service to fetch customer data
        Map<String, Object> customerData = itemService.getCustomerData(username);

        if (customerData != null) {
            return ResponseEntity.ok(customerData);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Customer not found: " + username));
        }
    }
}


