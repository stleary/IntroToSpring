package com.example.config;

import com.example.repository.JdbcGreetingRepository;
import com.example.repository.GreetingRepository;
import com.example.service.FormalGreetingService;
import com.example.service.GreetingService;
import com.example.controller.GreetingController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import javax.sql.DataSource;

@Configuration
public class AppConfig {
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setUrl(System.getenv("DB_URL"));
        ds.setUsername(System.getenv("DB_USERNAME"));
        ds.setPassword(System.getenv("DB_PASSWORD"));
        return ds;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public GreetingRepository greetingRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcGreetingRepository(jdbcTemplate);
    }

    @Bean
    public GreetingService greetingService(GreetingRepository greetingRepository) {
        return new FormalGreetingService(greetingRepository);
    }

    @Bean
    public GreetingController greetingController(GreetingService greetingService) {
        return new GreetingController(greetingService);
    }
}