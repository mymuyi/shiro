package chapter6.dao;

import chapter6.JdbcTemplateUtils;
import chapter6.entity.Permission;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @Author: yangke
 * @Date: 2019/8/30
 */
public class PermissionDaoImpl implements PermissionDao {

    private JdbcTemplate jdbcTemplate = JdbcTemplateUtils.jdbcTemplate();

    @Override
    public Permission createPermission(final Permission permission) {
        final String sql = "insert into sys_permission(permission, description, available) values(?,?,?)";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement statement = connection.prepareStatement(sql, new String[]{"id"});
                statement.setString(1, permission.getPermission());
                statement.setString(2, permission.getDescription());
                statement.setBoolean(3, permission.getAvailable());
                return statement;
            }
        }, keyHolder);
        permission.setId(keyHolder.getKey().longValue());

        return permission;
    }

    @Override
    public void deletePermission(Long permissionId) {
        String sql = "delete from sys_role_permission where permission_id = ?";
        jdbcTemplate.update(sql, permissionId);

        sql = "delete from sys_permission where id = ?";
        jdbcTemplate.update(sql, permissionId);
    }
}
