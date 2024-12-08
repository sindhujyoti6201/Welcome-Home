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

    public boolean isUserManager(String username) {
        Map<String, String> params = Collections.singletonMap("username", username);
        String sql = loadSqlFromFile("sql/delivery/check-manager-role.sql", params);

        try {
            int count = jdbcTemplate.queryForObject(sql, Integer.class);
            return count > 0;
        } catch (Exception e) {
            logger.severe("Error checking manager role: " + e.getMessage());
            return false;
        }
    }

    public Map<String, Object> getDeliveryOrders(String username) {
        Map<String, Object> result = new HashMap<>();
        Map<String, String> params = Collections.singletonMap("username", username);

        // Check if user is manager
        boolean isManager = isUserManager(username);
        String sql;

        if (isManager) {
            sql = loadSqlFromFile("sql/delivery/get-all-intransit-orders.sql", params);
        } else {
            sql = loadSqlFromFile("sql/delivery/get-delivery-orders.sql", params);
        }

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
            // Check if user is manager
            boolean isManager = isUserManager(username);

            if ("deliver".equals(action)) {
                Map<String, String> params = new HashMap<>();
                params.put("orderID", orderId.toString());

                // Use different update SQL based on role
                String updateDeliverySql;
                if (isManager) {
                    updateDeliverySql = loadSqlFromFile("sql/delivery/update-delivery-status-manager.sql", params);
                } else {
                    params.put("username", username);
                    updateDeliverySql = loadSqlFromFile("sql/delivery/update-delivery-status.sql", params);
                }

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
                Map<String, String> params = new HashMap<>();
                params.put("orderID", orderId.toString());

                // Use different delete SQL based on role
                String deleteDeliverySql;
                if (isManager) {
                    deleteDeliverySql = loadSqlFromFile("sql/delivery/delete-delivery-manager.sql", params);
                } else {
                    params.put("username", username);
                    deleteDeliverySql = loadSqlFromFile("sql/delivery/delete-delivery.sql", params);
                }

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