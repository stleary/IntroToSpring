package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class JdbcGreetingRepository implements GreetingRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcGreetingRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * RowMapper is an anonymous inner class that converts a db row to a Java class
     */
    private RowMapper<GreetingModel> rowMapper = new RowMapper<GreetingModel>() {

        @Override
        public GreetingModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            GreetingModel greetingModel = new GreetingModel();
            greetingModel.setId(rs.getLong("id"));
            greetingModel.setName(rs.getString("name"));
            greetingModel.setMessage(rs.getString("message"));
            greetingModel.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            return greetingModel;
        }
    };

    /**
     * Retrieve all greetings from the database.
     * 
     * @return List of all GreetingModel objects
     */
    @Override
    public List<GreetingModel> findAll() {
        return jdbcTemplate.query("SELECT * FROM greetings ORDER BY id", rowMapper);
    }

    /**
     * Find a greeting by name.
     * 
     * @param name the name to look up
     * @return the greeting if found, null otherwise
     */
    @Override
    public GreetingModel findByName(String name) {
        GreetingModel greetingModel = null;
        List<GreetingModel> results = jdbcTemplate.query("SELECT * FROM greetings WHERE name = ?", rowMapper, name);
        if (results.size() == 1) {
            greetingModel = results.get(0);
        }
        return greetingModel;
    }

    /**
     * Insert a new greeting into the database. An exception will be thrown on error
     * 
     * @param greetingModel the greeting to save
     */
    @Override
    public GreetingModel save(GreetingModel greetingModel) {
            jdbcTemplate.update("INSERT INTO greetings (name, message) VALUES (?, ?)", greetingModel.getName(),
                    greetingModel.getMessage());

            // This may be more common in the field:
            // Instead of passing a query string, use a lambda that creates a
            // PreparedStatement and populates a keyHolder that contains the
            // newly crested ID
            // KeyHolder keyHolder = new GeneratedKeyHolder();
            // jdbcTemplate.update(connection -> {
            // PreparedStatement ps = connection.prepareStatement(
            // "INSERT INTO greetings (name, message) VALUES (?, ?)",
            // Statement.RETURN_GENERATED_KEYS);
            // ps.setString(1, greetingModel.getName());
            // ps.setString(2, greetingModel.getMessage());
            // return ps;
            // }, keyHolder);
            // SQLite returns the key directly; other databases may differ
            // Long generatedId = keyHolder.getKey().longValue();
            return findByName(greetingModel.getName());
    }

    /**
     * Update an existing greeting.
     * 
     * @param greetingModel the greeting with updated values (must have a valid id)
     * @return number of rows affected (1 if successful, 0 if id not found)
     */
    @Override
    public int update(GreetingModel greetingModel) {
        return jdbcTemplate.update("UPDATE greetings SET name = ?, message = ? WHERE id = ?", greetingModel.getName(),
                greetingModel.getMessage(), greetingModel.getId());
    }

    /**
     * Delete a greeting by name.
     * 
     * @param name the greeting name to delete
     * @return number of rows affected (1 if successful, 0 if id not found)
     */
    @Override
    public int delete(String name) {
        return jdbcTemplate.update("DELETE FROM greetings WHERE name = ?", name);
    }
}
