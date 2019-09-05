package chapter6.dao;

import chapter6.JdbcTemplateUtils;
import chapter6.entity.User;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Author: yangke
 * @Date: 2019/8/28
 */
public class UserDaoImpl implements UserDao {

    private JdbcTemplate jdbcTemplate = JdbcTemplateUtils.jdbcTemplate();

    @Override
    public User createUser(final User user) {
        final String sql = "insert into sys_user(username, password, salt, locked) values(?,?,?,?)";

        // 用于获取自增主键值
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement statement = connection.prepareStatement(sql, new String[]{"id"});
                statement.setString(1, user.getUsername());
                statement.setString(2, user.getPassword());
                statement.setString(3, user.getSalt());
                statement.setBoolean(4, user.getLocked());
                return statement;
            }
        }, keyHolder);
        user.setId(keyHolder.getKey().longValue());
        return user;
    }

    @Override
    public void updateUser(User user) {
        String sql = "update sys_user set username=?, password=?, salt=?, locked=? where id=?";
        jdbcTemplate.update(sql, user.getUsername(), user.getPassword(), user.getSalt(), user.getLocked(), user.getId());
    }

    @Override
    public void deleteUser(Long userId) {
        String sql = "delete from sys_user where id = ?";
        jdbcTemplate.update(sql, userId);
    }

    @Override
    public void correlationRoles(Long userId, Long... roleIds) {
        if (userId == null || roleIds.length == 0) {
            return;
        }

        String sql = "insert into sys_user_role(user_id, role_id) values(?,?)";
        for (Long roleId : roleIds) {
            if (!exists(userId, roleId)) {
                jdbcTemplate.update(sql, userId, roleId);
            }
        }
    }

    @Override
    public void uncorrelationRoles(Long userId, Long... roleIds) {
        if (userId == null || roleIds.length == 0) {
            return;
        }

        String sql = "delete from sys_user_role where user_id = ? and role_id = ?";
        for (Long roleId : roleIds) {
            if (exists(userId, roleId)) {
                jdbcTemplate.update(sql, userId, roleId);
            }
        }
    }

    @Override
    public boolean exists(Long userId, Long roleId) {
        String sql = "select count(1) from sys_user_role where user_id = ? and role_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, userId, roleId) != 0;
    }

    @Override
    public User findById(Long userId) {
        String sql = "select id, username, password, salt, locked from sys_user where id = ?";
        List<User> userList = jdbcTemplate.query(sql, new BeanPropertyRowMapper(User.class), userId);
        if (userList.size() == 0) {
            return null;
        }
        return userList.get(0);
    }

    @Override
    public User findByUsername(String username) {
        String sql = "select id, username, password, salt, locked from sys_user where username = ?";
        List<User> userList = jdbcTemplate.query(sql, new BeanPropertyRowMapper(User.class), username);
        if (userList.size() == 0) {
            return null;
        }
        return userList.get(0);
    }

    @Override
    public Set<String> findRoles(String username) {
        String sql = "select role from sys_user u, sys_role r, sys_user_role ur " +
                "where u.username = ? and u.id = ur.user_id and ur.role_id = r.id";
        return new HashSet<>(jdbcTemplate.queryForList(sql, String.class, username));
    }

    @Override
    public Set<String> findPermissions(String username) {
        String sql = "select permission from sys_user u, sys_user_role ur, " +
                "sys_role r, sys_role_permission rp, sys_permission p " +
                "where u.username = ? and u.id = ur.user_id and ur.role_id = r.id " +
                "and r.id = rp.role_id and rp.permission_id = p.id";
        return new HashSet<>(jdbcTemplate.queryForList(sql, String.class, username));
    }
}
