package edu.nyu.welcomehome.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class MiscService {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    MiscService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public byte[] getImageById(int id) {
        String sql = "SELECT ImageData FROM Images WHERE ImageID = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, byte[].class);
    }
}
