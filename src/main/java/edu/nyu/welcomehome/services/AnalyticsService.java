package edu.nyu.welcomehome.services;

import edu.nyu.welcomehome.models.response.DonatedItemCategoryResponse;
import edu.nyu.welcomehome.models.response.ImmutableDonatedItemCategoryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static edu.nyu.welcomehome.utils.SqlFileLoader.loadSqlFromFile;

@Service
public class AnalyticsService {
    private static final Logger logger = Logger.getLogger(AnalyticsService.class.getName());

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Integer getTotalClientsServed() {
        String sql = loadSqlFromFile("sql/analytics/total-clients-served.sql", Collections.emptyMap());
        logger.info("The parsed query for getting total clients served: "+sql);

        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    public List<DonatedItemCategoryResponse> getItemCategoryDonations() {
        String sql = loadSqlFromFile("sql/analytics/item-category-donations.sql", Collections.emptyMap());
        logger.info("The parsed query for getting item category donations: " + sql);

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

    public Integer getTotalItemsDonated() {
        String sql = loadSqlFromFile("sql/analytics/total-items-donated.sql", Collections.emptyMap());
        logger.info("The parsed query for getting total items donated: " + sql);

        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    public Double getOrderCompletionRate() {
        String sql = loadSqlFromFile("sql/analytics/order-completion-rate.sql", Collections.emptyMap());
        logger.info("The parsed query for getting order completion rate: " + sql);

        return jdbcTemplate.queryForObject(sql, Double.class);
    }

    public List<DonatedItemCategoryResponse> getTopDonatedCategories(int topN) {
        Map<String, String> params = Collections.singletonMap("topN", String.valueOf(topN));
        String sql = loadSqlFromFile("sql/analytics/top-donated-categories.sql", params);
        logger.info("The parsed query for getting top donated categories: " + sql);

        // RowMapper to map the result set to DonatedItemCategoryResponse
        RowMapper<DonatedItemCategoryResponse> rowMapper = (rs, rowNum) ->
                ImmutableDonatedItemCategoryResponse.builder()
                        .mainCategory(rs.getString("mainCategory"))
                        .itemCount(rs.getInt("itemCount"))
                        .build();

        // Query the database using the RowMapper and passing the topN parameter for LIMIT
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Map<String, Object> getVolunteerContributions() {
        String sql = loadSqlFromFile("sql/analytics/volunteer-contributions.sql", Collections.emptyMap());
        logger.info("The parsed query for getting volunteer contributions: " + sql);

        return jdbcTemplate.queryForList(sql).get(0);
    }

    public Map<String, Object> getAverageProcessingTime() {
        String sql = loadSqlFromFile("sql/analytics/average-processing-time.sql", Collections.emptyMap());
        logger.info("The parsed query for getting average processing time: " + sql);

        return jdbcTemplate.queryForList(sql).get(0);
    }
}