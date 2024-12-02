package edu.nyu.welcomehome.services;

import edu.nyu.welcomehome.models.request.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
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
            // Fetch the stored password hash
            Map<String, String> params = Collections.singletonMap("username", username);
            String query = loadSqlFromFile("sql/volunteer-login.sql", params);
            logger.info("The query parsed is: " + query);
            String storedPasswordHash = jdbcTemplate.queryForObject(query, String.class);
            if (storedPasswordHash == null) {
                return false;
            }

            // Combine password with username (acting as salt) and hash it
            String hashedPassword = hashPasswordWithUsernameSalt(password, username);
            return hashedPassword.equals(storedPasswordHash);
        } catch (Exception e) {
            logger.info(String.format("Authorization error: %s", e));
            return false;
        }
    }

    public String hashPasswordWithUsernameSalt(String password, String username) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(username.getBytes());  // Using username as salt
        byte[] hashedPasswordBytes = md.digest(password.getBytes());
        return Base64.getEncoder().encodeToString(hashedPasswordBytes);
    }

    public void saveUser(RegisterRequest request) throws NoSuchAlgorithmException {
        Map<String, String> params = new HashMap<>();
        params.put("username", request.username());
        params.put("password", hashPasswordWithUsernameSalt(request.password(), request.username()));
        params.put("firstName", request.firstName());
        params.put("lastName", request.lastName());
        params.put("email", request.email());
        List<String> roleTypes = request.roleEnrolled();
//        roleTypes.add("staff");
//
//        // store details in the Person's table
        String query = loadSqlFromFile("sql/register.sql", params);
        logger.info("The query parsed is: " + query);
        jdbcTemplate.update(query);

        roleTypes.forEach(role -> {
            params.put("rDescription", role.toString());
            String queryToGetRoleID = loadSqlFromFile("sql/selectRoleID.sql", params);
            logger.info("The query parsed is: " + queryToGetRoleID);
            Integer roleID = jdbcTemplate.queryForObject(queryToGetRoleID, Integer.class);
            params.put("roleID", String.valueOf(roleID));
            String queryToSaveUserEnrolledRoles = loadSqlFromFile("sql/role.sql", params);
            logger.info("The query parsed is: " + queryToSaveUserEnrolledRoles);
            jdbcTemplate.update(queryToSaveUserEnrolledRoles);
        });
    }
}
