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

            for (Map.Entry<String, String> entry : params.entrySet()) {
                sqlTemplate = sqlTemplate.replace("{" + entry.getKey()+ "}", "'" + entry.getValue() + "'");
            }
            return sqlTemplate;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load SQL from the file");
        }
    }
}
