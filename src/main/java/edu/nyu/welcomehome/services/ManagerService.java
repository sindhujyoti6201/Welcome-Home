package edu.nyu.welcomehome.services;

import edu.nyu.welcomehome.models.ImmutableOrdered;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManagerService {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ManagerService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<ImmutableOrdered> getStartedOrders() {
        String sql = """
            SELECT o.* 
            FROM Ordered o 
            WHERE o.orderID NOT IN (
                SELECT orderID 
                FROM Delivered
            )
            ORDER BY o.orderDate DESC
        """;

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                ImmutableOrdered.builder()
                        .orderID(rs.getLong("orderID"))
                        .orderDate(rs.getDate("orderDate").toString())
                        .orderNotes(rs.getString("orderNotes"))
                        .supervisor(rs.getString("supervisor"))
                        .client(rs.getString("client"))
                        .build()
        );
    }

    public ImmutableOrdered getOrderById(Long orderId) {
        String sql = "SELECT * FROM Ordered WHERE orderID = ?";

        return jdbcTemplate.query(sql, new Object[]{orderId}, (rs, rowNum) ->
                ImmutableOrdered.builder()
                        .orderID(rs.getLong("orderID"))
                        .orderDate(rs.getDate("orderDate").toString())
                        .orderNotes(rs.getString("orderNotes"))
                        .supervisor(rs.getString("supervisor"))
                        .client(rs.getString("client"))
                        .build()
        ).stream().findFirst().orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public boolean isStaffMember(String username) {
        String sql = """
            SELECT COUNT(*) 
            FROM Act 
            WHERE userName = ? AND roleID IN ('manager', 'supervisor', 'delivery')
        """;

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, username);
        return count != null && count > 0;
    }

    public boolean isValidClient(String username) {
        String sql = "SELECT COUNT(*) FROM Person WHERE userName = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, username);
        return count != null && count > 0;
    }

    public Long createOrder(String clientUsername, String supervisorUsername, String notes) {
        if (!isValidClient(clientUsername)) {
            throw new RuntimeException("Invalid client username");
        }

        String sql = """
            INSERT INTO Ordered (orderDate, orderNotes, supervisor, client)
            VALUES (CURDATE(), ?, ?, ?)
        """;

        jdbcTemplate.update(sql, notes, supervisorUsername, clientUsername);

        return jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
    }
}
