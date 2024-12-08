package edu.nyu.welcomehome.controllers;

import edu.nyu.welcomehome.services.OrderHistoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;


@Controller
public class OrderController {

    private final OrderHistoryService orderHistoryService;

    public OrderController(OrderHistoryService orderHistoryService) {
        this.orderHistoryService = orderHistoryService;
    }

    @GetMapping("/orderhistory")
    public String getOrderHistory(Model model,
                                  @RequestParam(required = false) String username) {
        List<Map<String, Object>> orders = orderHistoryService.getOrders(username);
        System.out.println("Orders fetched: " + orders);

        // Fetch the orders using the username
        model.addAttribute("orders", orders);
        model.addAttribute("username", username);  // Add username to the model for use in the view
        return "orderhistory";  // The name of the HTML file (orderhistory.html)
    }
}

