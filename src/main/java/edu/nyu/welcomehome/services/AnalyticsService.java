package edu.nyu.welcomehome.services;

import edu.nyu.welcomehome.models.response.DonatedItemCategoryResponse;
import edu.nyu.welcomehome.models.response.ImmutableDonatedItemCategoryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AnalyticsService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int getTotalClientsServed() {
        return jdbcTemplate.queryForObject(
                "SELECT COUNT(DISTINCT client) FROM Ordered",
                Integer.class
        );
    }

    public List<DonatedItemCategoryResponse> getItemCategoryDonations() {
        String sql = "SELECT i.mainCategory, i.subCategory, COUNT(*) as itemCount " +
                "FROM Item i JOIN Category c ON i.mainCategory = c.mainCategory AND i.subCategory = c.subCategory " +
                "GROUP BY i.mainCategory, i.subCategory";

        // Define the RowMapper for mapping the result set to DonatedItemCategoryResponse
        RowMapper<DonatedItemCategoryResponse> rowMapper = (rs, rowNum) ->
                ImmutableDonatedItemCategoryResponse.builder()
                        .mainCategory(rs.getString("mainCategory"))
                        .subCategory(rs.getString("subCategory"))
                        .itemCount(rs.getInt("itemCount"))
                        .build();

        // Query the database and map results using the RowMapper
        return jdbcTemplate.query(sql, rowMapper);
    }

    public int getTotalItemsDonated() {
        return jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM Item",
                Integer.class
        );
    }

    public double getOrderCompletionRate() {
        return jdbcTemplate.queryForObject(
                "SELECT " +
                        "(CAST(SUM(CASE WHEN deliveredStatus = 'DELIVERED' THEN 1 ELSE 0 END) AS FLOAT) / COUNT(*)) * 100 " +
                        "FROM Delivered",
                Double.class
        );
    }

    public List<DonatedItemCategoryResponse> getTopDonatedCategories(int topN) {
        // SQL Query to get the top N donated categories based on item count
        String sql = "SELECT i.mainCategory, COUNT(*) as itemCount " +
                "FROM Item i " +
                "GROUP BY i.mainCategory " +
                "ORDER BY itemCount DESC " +
                "LIMIT ?";

        // RowMapper to map the result set to DonatedItemCategoryResponse
        RowMapper<DonatedItemCategoryResponse> rowMapper = (rs, rowNum) ->
                ImmutableDonatedItemCategoryResponse.builder()
                        .mainCategory(rs.getString("mainCategory"))
                        .itemCount(rs.getInt("itemCount"))
                        .build();

        // Query the database using the RowMapper and passing the topN parameter for LIMIT
        return jdbcTemplate.query(sql, rowMapper, topN);
    }

    public Map<String, Object> getVolunteerContributions() {
        return jdbcTemplate.queryForList(
                "SELECT " +
                        "COUNT(DISTINCT userName) as totalVolunteers, " +
                        "COUNT(DISTINCT ItemID) as itemsContributed " +
                        "FROM DonatedBy"
        ).get(0);
    }

    public Map<String, Object> getAverageProcessingTime() {
        return jdbcTemplate.queryForList(
                "SELECT " +
                        "AVG(DATEDIFF(Delivered.date, Ordered.orderDate)) as avgProcessingDays " +
                        "FROM Ordered " +
                        "JOIN Delivered ON Ordered.orderID = Delivered.orderID"
        ).get(0);
    }
}