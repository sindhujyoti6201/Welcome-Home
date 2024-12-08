package edu.nyu.welcomehome.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ItemService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ItemService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public Map<String, Object> getCustomerData(String username) {
        String query = "SELECT * FROM person WHERE username = ?";
        List<Map<String, Object>> results = jdbcTemplate.queryForList(query, username);
        if (results.isEmpty()) {
            return null; // Customer not found
        }
        return results.get(0); // Return first customer data
    }


    // Existing methods...
    public Set<String> getMainCategories() {
        String query = "SELECT DISTINCT mainCategory FROM ITEM";
        List<Map<String, Object>> results = jdbcTemplate.queryForList(query);

        Set<String> categories = new HashSet<>();
        for (Map<String, Object> row : results) {
            categories.add((String) row.get("mainCategory"));
        }
        return categories;
    }

    // Retrieve subcategories for a given main category
    public Set<String> getSubcategoriesByMainCategory(String mainCategory) {
        String query = "SELECT DISTINCT subCategory FROM ITEM WHERE mainCategory = ?";
        List<Map<String, Object>> results = jdbcTemplate.queryForList(query, mainCategory);

        Set<String> subcategories = new HashSet<>();
        for (Map<String, Object> row : results) {
            subcategories.add((String) row.get("subCategory"));
        }
        return subcategories;
    }

    // Search and filter items
    public List<Map<String, Object>> searchAndFilterItems(String itemId, String mainCategory, String subCategory) {
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM ITEM\n" +
                "WHERE itemId NOT IN (SELECT itemId FROM ITEMIN)\n");
        List<Object> params = new ArrayList<>();

        if (itemId != null && !itemId.trim().isEmpty()) {
            queryBuilder.append(" AND itemID LIKE ?");
            params.add("%" + itemId + "%");
        }

        if (mainCategory != null && !mainCategory.trim().isEmpty()) {
            queryBuilder.append(" AND mainCategory = ?");
            params.add(mainCategory);
        }

        if (subCategory != null && !subCategory.trim().isEmpty()) {
            queryBuilder.append(" AND subCategory = ?");
            params.add(subCategory);
        }

        return jdbcTemplate.queryForList(queryBuilder.toString(), params.toArray());
    }

    public boolean addItemToCart(String username, String itemId) {
        try {
            // Step 1: Create a new order in the 'Ordered' table with status 'INITIATED'
            String createOrderQuery = "INSERT INTO Ordered (client, orderStatus, orderDate) VALUES (?, 'INITIATED', NOW())";
            jdbcTemplate.update(createOrderQuery, username);
            //System.out.println("Order created for user: " + username);


            String getOrderIDQuery = "SELECT orderID FROM Ordered WHERE client = ? AND orderStatus = 'INITIATED' ORDER BY orderDate DESC, orderID DESC LIMIT 1";
            Integer newOrderId = jdbcTemplate.queryForObject(getOrderIDQuery, new Object[]{username}, Integer.class);
            //System.out.println("Generated Order ID: " + newOrderId);


            // Step 3: Insert the item into the 'ItemIn' table linked to the new order
            String addItemQuery = "INSERT INTO ItemIn (orderID, itemID) VALUES (?, ?)";
            jdbcTemplate.update(addItemQuery, newOrderId, itemId);
            //System.out.println("Item added to Order ID: " + newOrderId + " with Item ID: " + itemId);

            return true; // If everything succeeds, return true
        } catch (Exception e) {
            e.printStackTrace(); // Log the error
            System.out.println("Error adding item to cart for user: " + username + " with Item ID: " + itemId);
            return false; // Return false if there's any failure
        }
    }



    // Retrieve cart items for a specific user
//    public List<Map<String, Object>> getCartItems(String username) {
//        String query = "SELECT i.* FROM item i INNER JOIN cart c ON i.itemID = c.itemID WHERE c.userName = ?";
//        return jdbcTemplate.queryForList(query, username);
//    }
}