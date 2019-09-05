package chapter6.service;

import chapter6.dao.UserDao;
import chapter6.dao.UserDaoImpl;
import chapter6.entity.User;

import java.util.Set;

/**
 * @Author: yangke
 * @Date: 2019/8/30
 */
public class UserServiceImpl implements UserService {

    private PasswordHelper passwordHelper = new PasswordHelper();
    private UserDao userDao = new UserDaoImpl();

    @Override
    public User createUser(User user) {
        // 加密密码
        passwordHelper.encryptPassword(user);
        return userDao.createUser(user);
    }

    @Override
    public void changePassword(Long userId, String newPassword) {
        User user = userDao.findById(userId);
        user.setPassword(newPassword);
        passwordHelper.encryptPassword(user);
        userDao.updateUser(user);
    }

    @Override
    public void correlationRoles(Long userId, Long... roleIds) {
        userDao.correlationRoles(userId, roleIds);
    }

    @Override
    public void uncorrelationRoles(Long userId, Long... roleIds) {
        userDao.uncorrelationRoles(userId, roleIds);
    }

    @Override
    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public Set<String> findRoles(String username) {
        return userDao.findRoles(username);
    }

    @Override
    public Set<String> findPermissions(String username) {
        return userDao.findPermissions(username);
    }
}
