package edu.nyu.welcomehome.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {
    // Retrieve properties from VM arguments
    String DATABASE_NAME = System.getProperty("db.name", "your_database_name");
    String DATABASE_USERNAME = System.getProperty("db.username", "root"); // Default value is provided
    String DATABASE_PASSWORD = System.getProperty("db.password", "root"); // Default value is provided


    @Bean
    @Profile("dev") // This bean will only be created when the 'dev' profile is active
    public String databaseUrlDev() {
        // Development environment configuration
        return String.format("jdbc:mysql://localhost:3306/%s", DATABASE_NAME);
    }

    @Bean
    @Profile("prod") // This bean will only be created when the 'prod' profile is active
    public String databaseUrlProd() {
        // Production environment configuration
        return String.format("jdbc:mysql://localhost:3306/%s", DATABASE_NAME);
    }

    @Bean
    @Profile("dev")
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(databaseUrlDev());
        dataSource.setUsername(DATABASE_USERNAME);
        dataSource.setPassword(DATABASE_PASSWORD);
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
