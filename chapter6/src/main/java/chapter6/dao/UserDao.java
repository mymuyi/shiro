package chapter6.dao;

import chapter6.entity.User;

import java.util.Set;

/**
 * @Author: yangke
 * @Date: 2019/8/28
 */
public interface UserDao {

    /**
     * 创建用户
     * @param user
     * @return
     */
    User createUser(User user);

    /**
     * 更新用户
     * @param user
     */
    void updateUser(User user);

    /**
     * 删除用户
     * @param userId
     */
    void deleteUser(Long userId);

    /**
     * 关联用户和角色
     * @param userId
     * @param roleIds
     */
    void correlationRoles(Long userId, Long... roleIds);

    /**
     * 解除用户和角色的关联
     * @param userId
     * @param roleIds
     */
    void uncorrelationRoles(Long userId, Long... roleIds);

    /**
     * 用户和角色的关联是否已经存在
     * @param userId
     * @param roleId
     * @return
     */
    boolean exists(Long userId, Long roleId);

    /**
     * 根据 userId 查找用户
     * @param userId
     * @return
     */
    User findById(Long userId);

    /**
     * 根据 username 查找用户
     * @param username
     * @return
     */
    User findByUsername(String username);

    /**
     * 根据 username 查找对应用户拥有的角色
     * @param username
     * @return
     */
    Set<String> findRoles(String username);

    /**
     * 根据 username 查找对应用户拥有的权限
     * @param username
     * @return
     */
    Set<String> findPermissions(String username);

}
