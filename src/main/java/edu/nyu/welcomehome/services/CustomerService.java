package edu.nyu.welcomehome.services;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CustomerService {

    private final JdbcTemplate jdbcTemplate;

    public CustomerService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Fetches all customer details for the given username and returns the result as a list.
     */
    public List<Map<String, Object>> getCustomerDetails(String username) {
        String query = """
            SELECT c.customerID,
                    c.firstName,
                    c.lastName,
                    c.email,
                    c.phoneNumber
            FROM Customer c
            WHERE c.username = ?""";  // Parameterized query with `?`

        return jdbcTemplate.queryForList(query, username);  // Pass username parameter
    }
}
