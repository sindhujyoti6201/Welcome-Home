package edu.nyu.welcomehome.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
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

    public List<Map<String, Object>> getItemCategoryDonations() {
        return jdbcTemplate.queryForList(
                "SELECT i.mainCategory, i.subCategory, COUNT(*) as itemCount " +
                        "FROM Item i JOIN Category c ON i.mainCategory = c.mainCategory AND i.subCategory = c.subCategory " +
                        "GROUP BY i.mainCategory, i.subCategory"
        );
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
                        "(CAST(SUM(CASE WHEN status = 'COMPLETED' THEN 1 ELSE 0 END) AS FLOAT) / COUNT(*)) * 100 " +
                        "FROM Delivered",
                Double.class
        );
    }

    public List<Map<String, Object>> getTopDonatedCategories(int topN) {
        return jdbcTemplate.queryForList(
                "SELECT i.mainCategory, COUNT(*) as itemCount " +
                        "FROM Item i " +
                        "GROUP BY i.mainCategory " +
                        "ORDER BY itemCount DESC " +
                        "LIMIT ?",
                topN
        );
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