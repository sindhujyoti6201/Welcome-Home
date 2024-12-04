package edu.nyu.welcomehome.controllers;

import edu.nyu.welcomehome.services.OrderHistoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class OrderController {

    private final OrderHistoryService orderHistoryService;

    public OrderController(OrderHistoryService orderHistoryService) {
        this.orderHistoryService = orderHistoryService;
    }

    @GetMapping("/orderhistory")
    public String getOrderHistory(@RequestParam("username") String username, Model model) {
        // Fetch the orders from the service using the provided username
        model.addAttribute("orders", orderHistoryService.getOrders(username));
        return "orderhistory"; // The name of the HTML file (orderhistory.html)
    }
}
