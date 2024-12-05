package edu.nyu.welcomehome.services;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OrderHistoryService {

    private final JdbcTemplate jdbcTemplate;

    public OrderHistoryService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Fetches all orders for the given username and returns the result as a list.
     */
    public List<Map<String, Object>> getOrders(String username) {
        String query = """
            SELECT o.orderID,
                    o.orderDate,
                    d.status,
                    d.date AS deliveryDate,
                    i.iDescription AS itemDescription
            FROM Ordered o
            INNER JOIN Delivered d ON o.orderID = d.orderID
            INNER JOIN ItemIn ii ON o.orderID = ii.orderID
            INNER JOIN Item i ON ii.ItemID = i.ItemID
            WHERE o.client = ?""";  // Parameterized query with `?`

        return jdbcTemplate.queryForList(query, username);  // Pass username parameter
    }
}
