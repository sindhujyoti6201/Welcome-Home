package edu.nyu.welcomehome.controllers;


import edu.nyu.welcomehome.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/customer/cart")
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }


@PostMapping("/remove")
@ResponseBody
public ResponseEntity<String> removeItemFromCart(@RequestParam String username, @RequestParam String itemID) {
    System.out.println("Received request to remove item: " + itemID + " for user: " + username);
    try {
        boolean isRemoved = cartService.removeItemFromCart(username, itemID);
        if (isRemoved) {
            return ResponseEntity.ok("Item removed successfully!");
        } else {
            return ResponseEntity.status(400).body("Failed to remove item from the cart.");
        }
    } catch (Exception e) {
        System.err.println("Error in removeItemFromCart: " + e.getMessage());
        return ResponseEntity.status(500).body("An error occurred while removing the item.");
    }
}

}
