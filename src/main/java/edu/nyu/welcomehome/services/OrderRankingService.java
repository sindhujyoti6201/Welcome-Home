package edu.nyu.welcomehome.services;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class OrderRankingService {

    private final JdbcTemplate jdbcTemplate;

    public OrderRankingService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Fetches the most popular categories and subcategories for a given date range.
     * @param startDate Start date of the time period.
     * @param endDate End date of the time period.
     * @return A list of categories and subcategories ranked by order count.
     */
    public List<Map<String, Object>> getCategoryRankings(LocalDate startDate, LocalDate endDate) {
        System.out.println("Start Date: " + startDate);
        System.out.println("End Date: " + endDate);

        String query = """
            SELECT mainCategory,
                   subCategory,
                   orderCount
            FROM (
                SELECT\s
                    i.mainCategory,
                    i.subCategory,
                    COUNT(*) AS orderCount
                FROM Ordered o
                JOIN ItemIn ii ON o.orderID = ii.orderID
                JOIN Item i ON ii.ItemID = i.ItemID
                WHERE o.orderDate BETWEEN ? AND ?
                GROUP BY i.mainCategory, i.subCategory
                HAVING orderCount = (
                    SELECT MAX(orderCount)
                    FROM (
                        SELECT COUNT(*) AS orderCount
                        FROM Ordered o
                        JOIN ItemIn ii ON o.orderID = ii.orderID
                        JOIN Item i ON ii.ItemID = i.ItemID
                        WHERE o.orderDate BETWEEN ? AND ?
                        GROUP BY i.mainCategory, i.subCategory
                    ) AS MaxCounts
                )
            ) AS RankedCategories;
            """;

        try {
            return jdbcTemplate.queryForList(query, startDate, endDate, startDate, endDate);
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
    public List<Map<String, Object>> getCategoryOnlyRankings(LocalDate startDate, LocalDate endDate) {
        String query = """
            
                SELECT mainCategory, orderCount
            FROM (
                SELECT
                    i.mainCategory,
                    COUNT(*) AS orderCount
                FROM Ordered o
                JOIN ItemIn ii ON o.orderID = ii.orderID
                JOIN Item i ON ii.ItemID = i.ItemID
                WHERE o.orderDate BETWEEN ? AND ?
                GROUP BY i.mainCategory
            ) AS CategoryCounts
            WHERE orderCount = (
                SELECT MAX(orderCount)
                FROM (
                    SELECT COUNT(*) AS orderCount
                    FROM Ordered o
                    JOIN ItemIn ii ON o.orderID = ii.orderID
                    JOIN Item i ON ii.ItemID = i.ItemID
                    WHERE o.orderDate BETWEEN ? AND ?
                    GROUP BY i.mainCategory
                ) AS MaxCounts
            );
            """;

        try {
            return jdbcTemplate.queryForList(query, startDate, endDate, startDate, endDate);
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}