package edu.nyu.welcomehome.services;

import edu.nyu.welcomehome.models.request.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static edu.nyu.welcomehome.utils.SqlFileLoader.loadSqlFromFile;

@Service
public class UserAuthService {
    private final Logger logger = Logger.getLogger(UserAuthService.class.getName());
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    UserAuthService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean isAuthorizedUser(String username, String password) {
        try {
            Map<String, String> params = Collections.singletonMap("username", username);
            String query = loadSqlFromFile("sql/volunteer-login.sql", params);
            logger.info("The query parsed is: " + query);
            String storedPasswordHash = jdbcTemplate.queryForObject(query, String.class);
            if (storedPasswordHash == null) {
                return false;
            }

            String hashedPassword = hashPasswordWithUsernameSalt(password, username);
            return hashedPassword.equals(storedPasswordHash);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Authorization error", e);
            return false;
        }
    }

    public String hashPasswordWithUsernameSalt(String password, String username) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(username.getBytes());
        byte[] hashedPasswordBytes = md.digest(password.getBytes());
        return Base64.getEncoder().encodeToString(hashedPasswordBytes);
    }

    @Transactional
    public void saveUser(RegisterRequest request) throws NoSuchAlgorithmException {
        try {
            // First, save user details
            Map<String, String> params = new HashMap<>();
            params.put("username", request.username());
            params.put("password", hashPasswordWithUsernameSalt(request.password(), request.username()));
            params.put("firstName", request.firstName());
            params.put("lastName", request.lastName());
            params.put("email", request.email());

            String registerQuery = loadSqlFromFile("sql/register.sql", params);
            logger.info("Executing user registration query: " + registerQuery);
            jdbcTemplate.update(registerQuery);

            // Clear existing roles for this user
            String clearRolesQuery = "DELETE FROM Act WHERE username = ?";
            jdbcTemplate.update(clearRolesQuery, request.username());

            // Insert new roles
            for (String role : request.roleEnrolled()) {
                try {
                    String roleQuery = "INSERT INTO Act (username, roleID) VALUES (?, ?)";
                    int rowsAffected = jdbcTemplate.update(roleQuery, request.username(), role);
                    logger.info("Role assignment affected " + rowsAffected + " rows for role " + role);
                } catch (Exception e) {
                    logger.severe("Error assigning role " + role + " to user " + request.username() + ": " + e.getMessage());
                    throw e;
                }
            }
        } catch (Exception e) {
            logger.severe("Error in user registration process: " + e.getMessage());
            throw e;
        }
    }
}