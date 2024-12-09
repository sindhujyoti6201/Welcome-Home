package edu.nyu.welcomehome.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static edu.nyu.welcomehome.utils.SqlFileLoader.loadSqlFromFile;

@Service
public class ItemImageService {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    ItemImageService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public byte[] getImageById(int itemID) {
        Map<String, String> params = new HashMap<>();
        params.put("itemID", String.valueOf(itemID));
        String sql = loadSqlFromFile("sql/image/image.sql", params);
        return jdbcTemplate.queryForObject(sql, byte[].class);
    }
}
