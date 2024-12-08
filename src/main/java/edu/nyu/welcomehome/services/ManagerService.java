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
public class ManagerService {
    private final Logger logger = Logger.getLogger(ManagerService.class.getName());
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ManagerService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Map<String, Object> getInitiatedOrders() {
        Map<String, Object> result = new HashMap<>();

        String sql = loadSqlFromFile("sql/manager/get-initiated-orders.sql", Collections.emptyMap());

        try (Connection conn = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            List<Map<String, Object>> orders = new ArrayList<>();

            while (rs.next()) {
                Map<String, Object> order = new HashMap<>();
                order.put("orderID", rs.getLong("orderID"));
                order.put("orderDate", rs.getString("orderDate"));
                order.put("orderNotes", rs.getString("orderNotes"));
                order.put("client", rs.getString("client"));
                order.put("clientName", rs.getString("fname") + " " + rs.getString("lname"));
                order.put("orderStatus", rs.getString("orderStatus"));

                String supervisor = rs.getString("supervisor");
                if (supervisor != null) {
                    order.put("supervisor", supervisor);
                    order.put("supervisorName",
                            rs.getString("supervisorFname") + " " + rs.getString("supervisorLname"));
                }

                orders.add(order);
            }

            result.put("orders", orders);
        } catch (Exception e) {
            logger.severe("Error getting orders: " + e.getMessage());
            throw new RuntimeException("Failed to get orders", e);
        }

        return result;
    }

    private String getRandomUser(String role) throws Exception {
        Map<String, String> params = Collections.singletonMap("role", role);
        String sql = loadSqlFromFile("sql/manager/get-random-user.sql", params);

        try (Connection conn = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("userName");
            }
            throw new RuntimeException("No available " + role + " found");
        }
    }

    @Transactional
    public Map<String, Object> updateOrderStatus(Long orderId, String action) {
        try {
            if ("start".equals(action)) {
                String supervisor = getRandomUser("SUPERVISOR");
                String deliveryAgent = getRandomUser("DELIVERY AGENT");

                logger.info("Assigned supervisor: " + supervisor);
                logger.info("Assigned delivery agent: " + deliveryAgent);

                // Update order status
                Map<String, String> updateParams = new HashMap<>();
                updateParams.put("supervisor", supervisor);
                updateParams.put("orderID", orderId.toString());

                String updateOrderSql = loadSqlFromFile("sql/manager/update-order-start.sql", updateParams);

                try (Connection conn = jdbcTemplate.getDataSource().getConnection();
                     PreparedStatement stmt = conn.prepareStatement(updateOrderSql)) {
                    int rowsUpdated = stmt.executeUpdate();
                    if (rowsUpdated == 0) {
                        return Map.of("error", "Order not found or cannot be started");
                    }
                }

                // Insert delivery record
                Map<String, String> deliveryParams = new HashMap<>();
                deliveryParams.put("userName", deliveryAgent);
                deliveryParams.put("orderID", orderId.toString());

                String insertDeliverySql = loadSqlFromFile("sql/manager/insert-delivery.sql", deliveryParams);

                try (Connection conn = jdbcTemplate.getDataSource().getConnection();
                     PreparedStatement stmt = conn.prepareStatement(insertDeliverySql)) {
                    stmt.executeUpdate();
                }

                return Map.of(
                        "message", "Order started successfully",
                        "supervisor", supervisor,
                        "deliveryAgent", deliveryAgent
                );

            } else if ("cancel".equals(action)) {
                // Delete delivery record
                Map<String, String> deleteParams = Collections.singletonMap("orderID", orderId.toString());
                String deleteDeliverySql = loadSqlFromFile("sql/manager/delete-delivery.sql", deleteParams);

                try (Connection conn = jdbcTemplate.getDataSource().getConnection();
                     PreparedStatement stmt = conn.prepareStatement(deleteDeliverySql)) {
                    stmt.executeUpdate();
                }

                // Update order status
                String updateOrderSql = loadSqlFromFile("sql/manager/update-order-cancel.sql", deleteParams);

                try (Connection conn = jdbcTemplate.getDataSource().getConnection();
                     PreparedStatement stmt = conn.prepareStatement(updateOrderSql)) {
                    int rowsUpdated = stmt.executeUpdate();
                    if (rowsUpdated == 0) {
                        return Map.of("error", "Order not found or cannot be cancelled");
                    }
                }

                return Map.of("message", "Order cancelled successfully");
            }

            return Map.of("error", "Invalid action");

        } catch (Exception e) {
            logger.severe("Error updating order status: " + e.getMessage());
            throw new RuntimeException("Failed to update order status: " + e.getMessage());
        }
    }
}