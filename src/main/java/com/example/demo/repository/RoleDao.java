package com.example.demo.repository;

import com.example.demo.domain.Role;
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

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

// @Repository는 @Commpent 이고 컨테이너가 관리하는 Bean 이 된다. -> DataSource 를 생성한다.
@Repository
public class RoleDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final SimpleJdbcInsertOperations insertAction;

    public RoleDao(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        // DataSource를 사용하고, insert를 실행할 테이블을 withTableName에 넣어준다.
        this.insertAction = new SimpleJdbcInsert(dataSource).withTableName("role");
    }

    public boolean addRole(Role role) {
        // role은 프로퍼티 roleId, name
        // INSERT INTO role(role_id, name) VALUES(:roleId, :name);
        // :프로퍼티 -> NamedParameterJdbcTemplate 클래스가 이름 규칙을 찾아 값을 매칭한다.
        // 위와 같은 SQL을 SimpleJdbcInsert가 내부적으로 만든다.
        // Role 클래스의 프로퍼티이름과 칼럼명이 규칙이 맞아야 한다.
        // ex) Role 클래스의 roleId 프로퍼티
        //     Role 테이블의 role_id 칼럼

        SqlParameterSource params = new BeanPropertySqlParameterSource(role);
        int result = insertAction.execute(params);
        return result == 1;

    }

    public boolean deleteRole(int roleId) {
        String sql = "DELETE FROM role WHERE role_id = :roleId";
        SqlParameterSource params = new MapSqlParameterSource("roleId", roleId);
        int result = jdbcTemplate.update(sql, params);
        return result == 1; // SQL문이 정상적으로 실행될 경우 성공된 건수 1을 반환하기에 이를 ==조건식에 넣는다.
    }

    //Role 테이블 조회
    public Role getRole(int roleId) {
        String sql = "SELECT role_id, name FROM role WHERE role_id = :roleId";
        SqlParameterSource params = new MapSqlParameterSource("roleId", roleId);
        // Role클래스의 ResultSet을 자동으로 모두 넣어준다.
        RowMapper<Role> roleRowMapper = new BeanPropertyRowMapper<>(Role.class);
        return jdbcTemplate.queryForObject(sql,params, roleRowMapper);

    }

    public List<Role> getAllRoles() {
        String sql = "SELECT role_id, name FROM role";
        RowMapper<Role> roleRowMapper = new BeanPropertyRowMapper<>(Role.class);
        return jdbcTemplate.query(sql, roleRowMapper);
    }

}

// 클래스를 만드는 이유는 재사용을 하기 휘함인데
// getRole()메소드에서만 조회한 값을 담기위해 사용한다.
// 람다로 생략해 기존 코드에 포함한다.
//class RoleRowMapper implements RowMapper<Role> {
//    @Override
//    public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
//        Role role = new Role();
//        role.setRoleId(rs.getInt("role_id"));
//        role.setName(rs.getString("name"));
//        return role;
//    }
//}
