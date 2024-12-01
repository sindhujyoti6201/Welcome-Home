package edu.nyu.welcomehome.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.nyu.welcomehome.models.ImmutableDelivered;
import edu.nyu.welcomehome.models.ImmutableOrdered;
import edu.nyu.welcomehome.services.SuperviseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/supervise")
@CrossOrigin(origins = "*")
public class SuperviseController {
    private final SuperviseService superviseService;

    @Autowired
    public SuperviseController(SuperviseService superviseService) {
        this.superviseService = superviseService;
    }

    @GetMapping("/orders/{username}")
    public ResponseEntity<?> getOrders(@PathVariable String username) {
        try {
            Map<String, Object> response = superviseService.getOrderDetails(username);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/update-status")
    public ResponseEntity<?> updateStatus(@RequestBody String requestBody) {
        try {
            System.out.println("Received request body: " + requestBody);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(requestBody);

            ImmutableDelivered delivered = ImmutableDelivered.builder()
                    .userName(node.get("userName").asText())
                    .orderID(node.get("orderID").asLong())
                    .status(node.get("status").asText())
                    .date(node.get("date").asText())
                    .build();

            System.out.println("Created Delivered object: " + delivered);

            boolean updated = superviseService.updateDeliveryStatus(delivered);
            System.out.println("Update result: " + updated);

            return updated ? ResponseEntity.ok().build() :
                    ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
