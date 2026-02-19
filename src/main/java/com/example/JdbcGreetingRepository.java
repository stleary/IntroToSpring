package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcGreetingRepository  implements GreetingRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcGreetingRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * RowMapper is an anonymous inner class that converts a db row to a Java class
     */
    private final RowMapper<GreetingModel> rowMapper = new RowMapper<GreetingModel>() {
        @Override
        public GreetingModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            GreetingModel greetingModel = new GreetingModel();
            greetingModel.setId(rs.getLong("id"));
            greetingModel.setName(rs.getString("name"));
            greetingModel.setMessage(rs.getString("message"));
            greetingModel.setCreatedAt(rs.getTimestamp("created_at")
                    .toLocalDateTime());
            return greetingModel;
        }
    };

    /**
     * But sometimes you will see this Java lambda instead.
     * An anonymous function that takes two params, a resultSet and a row from the db,
     * and returns a GreetingModel instance
     */
//    private final RowMapper<GreetingModel> rowMapper = (rs, rowNum) -> {
//        GreetingModel greetingModel = new GreetingModel();
//        greetingModel.setId(rs.getLong("id"));
//        greetingModel.setName(rs.getString("name"));
//        greetingModel.setMessage(rs.getString("message"));
//        greetingModel.setCreatedAt(rs.getTimestamp("created_at")
//                .toLocalDateTime());
//        return greetingModel;
//    };


    /**
     * lookup in db by name
     * @param name the name to look up
     * @return Optional object, which may or may not contain a GreetingModel
     * Use isPresent() for presence, and get() if presence is true
     */
    @Override
    public Optional<GreetingModel> findByName(String name) {
        List<GreetingModel> results = jdbcTemplate.query(
                "SELECT * FROM greetings WHERE name = ?",
                rowMapper, name);
        return results.isEmpty() ?
                Optional.empty() : Optional.of(results.get(0));
    }
}