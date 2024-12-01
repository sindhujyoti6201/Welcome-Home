package edu.nyu.welcomehome.services;

import edu.nyu.welcomehome.models.request.DashboardAccessDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DashboardService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DashboardService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public DashboardAccessDTO getDashboardAccess(String username) throws SQLException {
        if (username == null || username.trim().isEmpty()) {
            throw new RuntimeException("Invalid username");
        }

        if (!userExists(username)) {
            throw new RuntimeException("User not found: " + username);
        }

        List<String> roles = checkRoles(username);

        if (roles.isEmpty()) {
            throw new RuntimeException("No roles assigned to user: " + username);
        }

        return new DashboardAccessDTO(username, roles);
    }

    private boolean userExists(String username) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Person WHERE userName = ?";
        try (Connection conn = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        }
    }

    private List<String> checkRoles(String username) throws SQLException {
        List<String> roles = new ArrayList<>();
        String sql = "SELECT LOWER(r.roleID) as roleID " +
                "FROM Act a " +
                "JOIN Role r ON a.roleID = r.roleID " +
                "WHERE LOWER(a.userName) = LOWER(?)";

        try (Connection conn = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                roles.add(rs.getString("roleID"));
            }
        }
        return roles;
    }
}
