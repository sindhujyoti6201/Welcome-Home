package edu.nyu.welcomehome.services;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static edu.nyu.welcomehome.utils.SqlFileLoader.loadSqlFromFile;

@Service
public class OrderHistoryService {
    private static final Logger logger = Logger.getLogger(OrderHistoryService.class.getName());
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
                    o.orderStatus,
                    i.iDescription AS itemDescription,
                    i.mainCategory AS itemMainCategory,
                    i.subCategory AS itemSubCategory
            FROM Ordered o
            INNER JOIN ItemIn ii ON o.orderID = ii.orderID
            INNER JOIN Item i ON ii.ItemID = i.ItemID
            WHERE o.client = ?""";  // Parameterized query with `?`

        return jdbcTemplate.queryForList(query, username);  // Pass username parameter
        Map<String, String> params = Collections.singletonMap("username", username);
        String query = loadSqlFromFile("sql/orderhistory.sql", params);
        logger.info("The query parsed for getting order history is: " + query);
        return jdbcTemplate.queryForList(query);  // Pass username parameter

    }
}
