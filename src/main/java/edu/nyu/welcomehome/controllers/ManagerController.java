package edu.nyu.welcomehome.controllers;

import edu.nyu.welcomehome.services.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/manager")
@CrossOrigin(origins = "*")
public class ManagerController {
    private final Logger logger = Logger.getLogger(ManagerController.class.getName());
    private final ManagerService managerService;

    @Autowired
    public ManagerController(ManagerService managerService) {
        this.managerService = managerService;
    }

    @GetMapping("/orders/{username}")
    public ResponseEntity<?> getOrders(@PathVariable String username) {
        try {
            logger.info("Fetching orders for manager: " + username);
            Map<String, Object> response = managerService.getInitiatedOrders();

            if (((List<?>) response.get("orders")).isEmpty()) {
                logger.info("No orders found for manager");
                return ResponseEntity.ok()
                        .body(Map.of(
                                "orders", List.of(),
                                "message", "No orders available at this time"
                        ));
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.severe("Error fetching orders: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch orders: " + e.getMessage()));
        }
    }

    @PostMapping("/update-order")
    public ResponseEntity<?> updateOrderStatus(@RequestBody Map<String, Object> request) {
        try {
            Long orderId = Long.valueOf(request.get("orderID").toString());
            String action = (String) request.get("action");

            logger.info("Updating order " + orderId + " with action: " + action);
            Map<String, Object> result = managerService.updateOrderStatus(orderId, action);

            if (result.containsKey("error")) {
                logger.warning("Failed to update order: " + result.get("error"));
                return ResponseEntity.badRequest()
                        .body(result);
            }

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            logger.severe("Error updating order status: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to update order status: " + e.getMessage()));
        }
    }
}
