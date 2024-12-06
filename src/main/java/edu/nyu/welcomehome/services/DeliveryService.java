package edu.nyu.welcomehome.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.util.logging.Logger;

import static edu.nyu.welcomehome.utils.SqlFileLoader.loadSqlFromFile;

@Service
public class DeliveryService {
    private final Logger logger = Logger.getLogger(DeliveryService.class.getName());
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DeliveryService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Map<String, Object> getDeliveryOrders(String username) {
        Map<String, Object> result = new HashMap<>();

        Map<String, String> params = Collections.singletonMap("username", username);
        String sql = loadSqlFromFile("sql/delivery/get-delivery-orders.sql", params);

        try (Connection conn = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            List<Map<String, Object>> orders = new ArrayList<>();

            while (rs.next()) {
                Map<String, Object> order = new HashMap<>();
                order.put("orderID", rs.getLong("orderID"));
                order.put("date", rs.getString("date"));
                order.put("deliveredStatus", rs.getString("deliveredStatus"));
                order.put("orderNotes", rs.getString("orderNotes"));
                order.put("orderStatus", rs.getString("orderStatus"));
                order.put("clientName", rs.getString("clientFname") + " " + rs.getString("clientLname"));
                order.put("supervisorName", rs.getString("supervisorFname") + " " + rs.getString("supervisorLname"));
                orders.add(order);
            }

            result.put("orders", orders);
        } catch (Exception e) {
            logger.severe("Error getting delivery orders: " + e.getMessage());
            throw new RuntimeException("Failed to get delivery orders", e);
        }

        return result;
    }

    @Transactional
    public boolean updateDeliveryStatus(String username, Long orderId, String action) {
        try {
            if ("deliver".equals(action)) {
                // Update delivery status
                Map<String, String> deliveryParams = new HashMap<>();
                deliveryParams.put("orderID", orderId.toString());
                deliveryParams.put("username", username);

                String updateDeliverySql = loadSqlFromFile("sql/delivery/update-delivery-status.sql", deliveryParams);

                try (Connection conn = jdbcTemplate.getDataSource().getConnection();
                     PreparedStatement stmt = conn.prepareStatement(updateDeliverySql)) {
                    int deliveryUpdated = stmt.executeUpdate();

                    if (deliveryUpdated == 0) {
                        return false;
                    }
                }

                // Update order status
                Map<String, String> orderParams = Collections.singletonMap("orderID", orderId.toString());
                String updateOrderSql = loadSqlFromFile("sql/delivery/update-order-complete.sql", orderParams);

                try (Connection conn = jdbcTemplate.getDataSource().getConnection();
                     PreparedStatement stmt = conn.prepareStatement(updateOrderSql)) {
                    return stmt.executeUpdate() > 0;
                }

            } else if ("cancel".equals(action)) {
                // Delete delivery record
                Map<String, String> deleteParams = new HashMap<>();
                deleteParams.put("orderID", orderId.toString());
                deleteParams.put("username", username);

                String deleteDeliverySql = loadSqlFromFile("sql/delivery/delete-delivery.sql", deleteParams);

                try (Connection conn = jdbcTemplate.getDataSource().getConnection();
                     PreparedStatement stmt = conn.prepareStatement(deleteDeliverySql)) {
                    stmt.executeUpdate();
                }

                // Update order status
                Map<String, String> orderParams = Collections.singletonMap("orderID", orderId.toString());
                String updateOrderSql = loadSqlFromFile("sql/delivery/update-order-cancel.sql", orderParams);

                try (Connection conn = jdbcTemplate.getDataSource().getConnection();
                     PreparedStatement stmt = conn.prepareStatement(updateOrderSql)) {
                    return stmt.executeUpdate() > 0;
                }
            }

            return false;

        } catch (Exception e) {
            logger.severe("Error updating delivery status: " + e.getMessage());
            throw new RuntimeException("Failed to update delivery status", e);
        }
    }
}