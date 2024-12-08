package edu.nyu.welcomehome.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CartService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

public boolean removeItemFromCart(String username, String itemID) {
    String query = "DELETE FROM Cart WHERE username = ? AND itemID = ?";
    System.out.println("Executing query: " + query + " with username: " + username + " and itemID: " + itemID);
    try {
        int rowsAffected = jdbcTemplate.update(query, username, itemID);
        if (rowsAffected > 0) {
            return true;
        } else {
            System.err.println("No rows affected. Item not found in the cart.");
            return false;
        }
    } catch (Exception e) {
        System.err.println("Error executing query: " + e.getMessage());
        return false;
    }
}

}
