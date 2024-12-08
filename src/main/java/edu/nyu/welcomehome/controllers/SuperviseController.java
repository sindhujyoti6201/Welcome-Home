package edu.nyu.welcomehome.controllers;

import edu.nyu.welcomehome.models.Delivered;
import edu.nyu.welcomehome.services.SuperviseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/supervise")
@CrossOrigin(origins = "*")
public class SuperviseController {
    private final Logger logger = Logger.getLogger(SuperviseController.class.getName());
    private final SuperviseService superviseService;

    @Autowired
    public SuperviseController(SuperviseService superviseService) {
        this.superviseService = superviseService;
    }

    @GetMapping("/orders/{username}")
    public ResponseEntity<?> getOrders(@PathVariable String username) {
        try {
            logger.info("Fetching orders for user: " + username);

            // First, check if the user is a manager
            boolean isManager = superviseService.isUserManager(username);
            Map<String, Object> response;

            if (isManager) {
                response = superviseService.getAllInProgressOrders();
            } else {
                response = superviseService.getOrderDetails(username);
            }

            if (response.isEmpty() || ((List<?>) response.get("orders")).isEmpty()) {
                logger.info("No orders found for user: " + username);
                return ResponseEntity.ok()
                        .body(Map.of(
                                "orders", List.of(),
                                "message", "No orders requiring supervision at this time"
                        ));
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.severe("Error fetching orders: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch orders: " + e.getMessage()));
        }
    }

    @PostMapping("/update-status")
    public ResponseEntity<?> updateStatus(@RequestBody Map<String, Object> request) {
        try {
            String username = (String) request.get("userName");
            Long orderId = Long.valueOf(request.get("orderID").toString());
            String date = (String) request.get("date");

            logger.info("Updating status for order " + orderId + " by user " + username);

            Delivered delivered = new Delivered();
            delivered.setUserName(username);
            delivered.setOrderID(orderId);
            delivered.setDeliveredStatus("IN_TRANSIT");
            delivered.setDate(date);

            boolean updated = superviseService.updateDeliveryStatus(delivered);

            if (!updated) {
                logger.warning("Failed to update order " + orderId + ". Invalid status or conditions not met.");
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Order cannot be updated. It must be NOT YET DELIVERED."));
            }

            return ResponseEntity.ok()
                    .body(Map.of("message", "Order successfully marked as In Transit"));

        } catch (Exception e) {
            logger.severe("Error updating order status: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to update order status: " + e.getMessage()));
        }
    }
}