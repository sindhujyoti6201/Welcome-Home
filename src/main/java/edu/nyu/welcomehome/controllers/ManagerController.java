package edu.nyu.welcomehome.controllers;

import edu.nyu.welcomehome.models.ImmutableOrdered;
import edu.nyu.welcomehome.services.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/manager")
@CrossOrigin(origins = "*")
public class ManagerController {
    private final ManagerService managerService;

    @Autowired
    public ManagerController(ManagerService managerService) {
        this.managerService = managerService;
    }

    @GetMapping("/started-orders")
    public ResponseEntity<?> getStartedOrders() {
        try {
            List<ImmutableOrdered> orders = managerService.getStartedOrders();
            return ResponseEntity.ok(Map.of("orders", orders));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getOrder(@PathVariable Long orderId) {
        try {
            ImmutableOrdered order = managerService.getOrderById(orderId);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/order/create")
    public ResponseEntity<?> createOrder(@RequestBody Map<String, String> request) {
        try {
            String clientUsername = request.get("clientUsername");
            String supervisorUsername = request.get("supervisorUsername");
            String notes = request.get("notes");

            if (!managerService.isStaffMember(supervisorUsername)) {
                return ResponseEntity.status(403).body(Map.of("error", "Not authorized"));
            }

            Long orderId = managerService.createOrder(clientUsername, supervisorUsername, notes);
            return ResponseEntity.ok(Map.of("orderId", orderId));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }
}
