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

    public boolean isAuthorizedAsStaff(String username, String password) {
        String sqlFilePath = "sql/auth/volunteer-login.sql";
        return isAuthorizedUser(username, password, sqlFilePath);
    }

    public boolean isAuthorizedAsCustomer(String username, String password) {
        String sqlFilePath = "sql/auth/customer-login.sql";
        return isAuthorizedUser(username, password, sqlFilePath);
    }

    public boolean isAuthorizedUser(String username, String password, String sqlFilePath) {
        try {
            // Fetch the stored password hash
            Map<String, String> params = Collections.singletonMap("username", username);
            String query = loadSqlFromFile(sqlFilePath, params);
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

    public void saveUserAsCustomer(RegisterRequest request) throws NoSuchAlgorithmException {
        List<String> roleTypes = new ArrayList<>();
        roleTypes.add("BORROWER");

        // store details in the Person's table
        saveUser(request, roleTypes);
    }
    public void saveUserAsVolunteer(RegisterRequest request) throws NoSuchAlgorithmException {
        List<String> roleTypes = new ArrayList<>();
        request.roleEnrolled().forEach(roleType -> roleTypes.add(roleType.getRole()));
        roleTypes.add("STAFF");

        // store details in the Person's table
        saveUser(request, roleTypes);
    }

    private void saveUser(RegisterRequest request, List<String> roleTypes) throws NoSuchAlgorithmException {
        Map<String, String> params = new HashMap<>();
        params.put("username", request.username());
        params.put("password", hashPasswordWithUsernameSalt(request.password(), request.username()));
        params.put("firstName", request.firstName());
        params.put("lastName", request.lastName());
        params.put("email", request.email());

        String query = loadSqlFromFile("sql/auth/register.sql", params);
        logger.info("The query parsed is: " + query);
        jdbcTemplate.update(query);

        roleTypes.forEach(role -> {
            params.put("roleID", role);
            String queryToGetRoleID = loadSqlFromFile("sql/auth/selectRoleID.sql", params);
            logger.info("The query parsed is: " + queryToGetRoleID);
            String roleID = jdbcTemplate.queryForObject(queryToGetRoleID, String.class);
            if (roleID != null) {
                String queryToSaveUserEnrolledRoles = loadSqlFromFile("sql/auth/role.sql", params);
                logger.info("The query parsed is: " + queryToSaveUserEnrolledRoles);
                jdbcTemplate.update(queryToSaveUserEnrolledRoles);
                System.out.println(roleID+" Completed");
            } else {
                throw new RuntimeException(String.format("roleID: %s not found", role));
            }
        });
    }
}