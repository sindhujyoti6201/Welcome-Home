package edu.nyu.welcomehome.controllers;

import edu.nyu.welcomehome.services.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/delivery")
@CrossOrigin(origins = "*")
public class DeliveryController {
    private final Logger logger = Logger.getLogger(DeliveryController.class.getName());
    private final DeliveryService deliveryService;

    @Autowired
    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @GetMapping("/orders/{username}")
    public ResponseEntity<?> getDeliveryOrders(@PathVariable String username) {
        try {
            Map<String, Object> response = deliveryService.getDeliveryOrders(username);

            if (((List<?>) response.get("orders")).isEmpty()) {
                return ResponseEntity.ok()
                        .body(Map.of(
                                "orders", List.of(),
                                "message", "No orders available for delivery"
                        ));
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.severe("Error fetching delivery orders: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/update-status")
    public ResponseEntity<?> updateDeliveryStatus(@RequestBody Map<String, Object> request) {
        try {
            String username = (String) request.get("userName");
            Long orderId = Long.valueOf(request.get("orderID").toString());
            String action = (String) request.get("action");

            boolean updated = deliveryService.updateDeliveryStatus(username, orderId, action);

            if (!updated) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Failed to update order status"));
            }

            return ResponseEntity.ok()
                    .body(Map.of("message", "Order status updated successfully"));

        } catch (Exception e) {
            logger.severe("Error updating delivery status: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
