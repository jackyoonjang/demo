package com.example.demo.repository;

import com.example.demo.domain.Role;
import com.example.demo.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.simple.SimpleJdbcInsertOperations;
import org.springframework.stereotype.Repository;

import javax.security.sasl.Sasl;
import javax.sql.DataSource;
import java.util.List;

@Repository
public class UserDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsertOperations insertAction;

    @Autowired
    public UserDao(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.insertAction = new SimpleJdbcInsert(dataSource).withTableName("user");
    }

    public boolean addUser(User user) {
        String sql = "insert into users (username, password, email) values (:name, :password, :email)";
        SqlParameterSource param =  new BeanPropertySqlParameterSource(user);

        int execute = insertAction.execute(param);
        return execute == 1;
    }


    public boolean deleteUser(int userId) {
        String sql = "delete from user where user_id = :userId";
        SqlParameterSource param =  new MapSqlParameterSource("userId",userId);
        return jdbcTemplate.update(sql, param) == 1;
    }

    public User getUser(int userId) {
        String sql = "select * from user where user_id = :userId";
        SqlParameterSource param =  new MapSqlParameterSource("userId",userId);
        RowMapper<User> roleRowMapper = new BeanPropertyRowMapper<>(User.class);
        return jdbcTemplate.queryForObject(sql,param,roleRowMapper);
    }

    public List<User> getAllUsers() {
        String sql = "select * from user";
        RowMapper<User> param = new BeanPropertyRowMapper<>(User.class);
        return jdbcTemplate.query(sql,param);
    }
}
