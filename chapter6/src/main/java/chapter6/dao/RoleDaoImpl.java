package chapter6.dao;

import chapter6.JdbcTemplateUtils;
import chapter6.entity.Role;
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
public class RoleDaoImpl implements RoleDao {

    private JdbcTemplate jdbcTemplate = JdbcTemplateUtils.jdbcTemplate();

    @Override
    public Role createRole(final Role role) {
        final String sql = "insert into sys_role(role, description, available) values(?, ?, ?)";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement statement = connection.prepareStatement(sql, new String[]{"id"});
                statement.setString(1, role.getRole());
                statement.setString(2, role.getDescription());
                statement.setBoolean(3, role.getAvailable());
                return statement;
            }
        }, keyHolder);

        role.setId(keyHolder.getKey().longValue());

        return role;
    }

    @Override
    public void deleteRole(Long roleId) {

        // 首先把和 role 关联的表数据删除
        String sql = "delete from sys_user_role where role_id = ?";
        jdbcTemplate.update(sql, roleId);

        sql = "delete from sys_role where id = ?";
        jdbcTemplate.update(sql, roleId);
    }

    @Override
    public void correlationPermissions(Long roleId, Long... permissionIds) {
        if (roleId == null || permissionIds.length == 0) {
            return;
        }
        for (Long permissionId : permissionIds) {
            if (!exists(roleId, permissionId)) {
                String sql = "insert into sys_role_permission(role_id, permission_id) values(?, ?)";
                jdbcTemplate.update(sql, roleId, permissionId);
            }
        }

    }

    @Override
    public void uncorrelationPermissions(Long roleId, Long... permissionIds) {
        if (roleId == null || permissionIds.length == 0) {
            return;
        }
        for (Long permissionId : permissionIds) {
            if (exists(roleId, permissionId)) {
                String sql = "delete from sys_role_permission where role_id = ? and permission_id = ?";
                jdbcTemplate.update(sql, roleId, permissionId);
            }
        }
    }

    @Override
    public boolean exists(Long roleId, Long permissionId) {
        String sql = "select count(1) from sys_role_permission where role_id = ? and permission_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, roleId, permissionId) != 0;
    }
}
