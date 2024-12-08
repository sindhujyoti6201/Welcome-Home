package edu.nyu.welcomehome.services;

import edu.nyu.welcomehome.models.request.DashboardAccessDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Logger;

import static edu.nyu.welcomehome.utils.SqlFileLoader.loadSqlFromFile;

@Service
public class DashboardService {
    private final Logger logger = Logger.getLogger(DashboardService.class.getName());
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
        logger.info("Found roles for " + username + ": " + roles);

        if (roles.isEmpty()) {
            throw new RuntimeException("No roles assigned to user: " + username);
        }

        return new DashboardAccessDTO(username, roles);
    }

    private boolean userExists(String username) throws SQLException {
        Map<String, String> params = Collections.singletonMap("username", username);
        String sql = loadSqlFromFile("sql/dashboard/check-user-exists.sql", params);

        try (Connection conn = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        }
    }

    private List<String> checkRoles(String username) throws SQLException {
        List<String> roles = new ArrayList<>();
        Map<String, String> params = Collections.singletonMap("username", username);
        String sql = loadSqlFromFile("sql/dashboard/check-user-roles.sql", params);

        try (Connection conn = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String role = rs.getString("roleID").trim();
                logger.info("Found role for " + username + ": " + role);
                roles.add(role);
            }
        }
        return roles;
    }
}