package edu.nyu.welcomehome.services;

import edu.nyu.welcomehome.models.ImmutableDelivered;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SuperviseService {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public SuperviseService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Map<String, Object> getOrderDetails(String username) {
        Map<String, Object> result = new HashMap<>();

        String orderSql = """
            SELECT o.*, 
                   COALESCE(d.status, 'pending') as currentStatus
            FROM Ordered o
            LEFT JOIN Delivered d ON o.orderID = d.orderID 
            AND d.date = (
                SELECT MAX(date) 
                FROM Delivered 
                WHERE orderID = o.orderID
            )
            WHERE o.supervisor = ?
            ORDER BY o.orderDate DESC
        """;

        List<Map<String, Object>> orders = jdbcTemplate.queryForList(orderSql, username);
        result.put("orders", orders);

        String statusSql = """
            SELECT d.*
            FROM Delivered d
            INNER JOIN Ordered o ON d.orderID = o.orderID
            WHERE o.supervisor = ?
            ORDER BY d.date DESC
        """;

        List<Map<String, Object>> deliveryStatus = jdbcTemplate.queryForList(statusSql, username);
        result.put("deliveryStatus", deliveryStatus);

        return result;
    }

    public boolean updateDeliveryStatus(ImmutableDelivered delivered) {
        try {
            String getClientSql = """
                SELECT client FROM Ordered WHERE orderID = ?
            """;

            String client = jdbcTemplate.queryForObject(
                    getClientSql,
                    String.class,
                    delivered.orderID()
            );

            if (client == null) {
                throw new IllegalArgumentException("Invalid orderID: Client not found");
            }

            String checkSql = """
                SELECT COUNT(*) FROM Delivered 
                WHERE userName = ? AND orderID = ?
            """;

            int exists = jdbcTemplate.queryForObject(
                    checkSql,
                    Integer.class,
                    client,
                    delivered.orderID()
            );

            if (exists > 0) {
                String updateSql = """
                    UPDATE Delivered 
                    SET status = ?, date = ? 
                    WHERE userName = ? AND orderID = ?
                """;

                return jdbcTemplate.update(
                        updateSql,
                        delivered.status(),
                        delivered.date(),
                        client,
                        delivered.orderID()
                ) > 0;
            } else {
                String insertSql = """
                    INSERT INTO Delivered (userName, orderID, status, date)
                    VALUES (?, ?, ?, ?)
                """;

                return jdbcTemplate.update(
                        insertSql,
                        client,
                        delivered.orderID(),
                        delivered.status(),
                        delivered.date()
                ) > 0;
            }
        } catch (Exception e) {
            System.out.println("Error updating delivery status: " + e.getMessage());
            return false;
        }
    }
}
