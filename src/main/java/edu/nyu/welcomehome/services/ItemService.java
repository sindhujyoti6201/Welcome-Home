package edu.nyu.welcomehome.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ItemService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ItemService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Retrieve unique main categories
    public Set<String> getMainCategories() {
        String query = "SELECT DISTINCT mainCategory FROM ITEM";
        List<Map<String, Object>> results = jdbcTemplate.queryForList(query);

        Set<String> categories = new HashSet<>();
        for (Map<String, Object> row : results) {
            categories.add((String) row.get("mainCategory"));
        }
        return categories;
    }

    // Retrieve subcategories for a given main category
    public Set<String> getSubcategoriesByMainCategory(String mainCategory) {
        String query = "SELECT DISTINCT subCategory FROM ITEM WHERE mainCategory = ?";
        List<Map<String, Object>> results = jdbcTemplate.queryForList(query, mainCategory);

        Set<String> subcategories = new HashSet<>();
        for (Map<String, Object> row : results) {
            subcategories.add((String) row.get("subCategory"));
        }
        return subcategories;
    }

    // Search and filter items
    public List<Map<String, Object>> searchAndFilterItems(String itemId, String mainCategory, String subCategory) {
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM ITEM WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (itemId != null && !itemId.trim().isEmpty()) {
            queryBuilder.append(" AND itemID LIKE ?");
            params.add("%" + itemId + "%");
        }

        if (mainCategory != null && !mainCategory.trim().isEmpty()) {
            queryBuilder.append(" AND mainCategory = ?");
            params.add(mainCategory);
        }

        if (subCategory != null && !subCategory.trim().isEmpty()) {
            queryBuilder.append(" AND subCategory = ?");
            params.add(subCategory);
        }

        return jdbcTemplate.queryForList(queryBuilder.toString(), params.toArray());
    }
}
