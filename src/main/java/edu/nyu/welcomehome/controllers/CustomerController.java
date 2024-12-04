package edu.nyu.welcomehome.controllers;

import edu.nyu.welcomehome.services.CustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@Controller
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/viewprofile")
    public String getCustomerDetails(Model model) {
        // Fetch the customer data (you can change 'john_doe' to dynamic username as needed)
        List<Map<String, Object>> customerDetails = customerService.getCustomerDetails("john_doe");

        // Add the data to the model so it can be used in the template
        model.addAttribute("customerDetails", customerDetails);

        // Return the 'customer.html' template
        return "viewprofile";
    }
}
