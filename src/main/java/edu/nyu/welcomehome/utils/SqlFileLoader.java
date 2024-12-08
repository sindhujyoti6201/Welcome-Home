package edu.nyu.welcomehome.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.stream.Collectors;

public class SqlFileLoader {
    // Private constructor to prevent instantiation
    private SqlFileLoader() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static String loadSqlFromFile(String fileName, Map<String, String> params) {
        try (InputStream inputStream = SqlFileLoader.class.getClassLoader().getResourceAsStream(fileName);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            String sqlTemplate = reader.lines().collect(Collectors.joining("\n"));

            // Iterate through the params map and replace placeholders with the actual values
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String placeholder = "{" + entry.getKey() + "}";
                String value = entry.getValue();
                if (value.chars().allMatch(Character::isDigit)) {
                    sqlTemplate = sqlTemplate.replace(placeholder, value);
                } else {
                    // Escape any single quotes in the value to prevent SQL injection
                    value = value.replace("'", "''");

                    // Replace the placeholder with the escaped value
                    sqlTemplate = sqlTemplate.replace(placeholder, "'" + value + "'");
                }
            }

            return sqlTemplate;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load SQL from the file", e);
        }
    }
}
