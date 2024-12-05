package edu.nyu.welcomehome.controllers;

import edu.nyu.welcomehome.models.response.SearchOrderResponse;
import edu.nyu.welcomehome.services.SearchOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class SearchOrderController {

    private final SearchOrderService searchOrderService;

    @Autowired
    public SearchOrderController(SearchOrderService searchOrderService) {
        this.searchOrderService = searchOrderService;
    }

    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SearchOrderResponse>> searchOrders(@RequestParam(required = false) String username,
                                                                  @RequestParam(required = false) String orderId) {
        System.out.println("Received parameters - username: " + username + ", orderId: " + orderId);
        try {
            List<SearchOrderResponse> response = searchOrderService.searchOrders(username, orderId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
