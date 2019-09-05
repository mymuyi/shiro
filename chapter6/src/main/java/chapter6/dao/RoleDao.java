package chapter6.dao;

import chapter6.entity.Role;

/**
 * @Author: yangke
 * @Date: 2019/8/28
 */
public interface RoleDao {

    /**
     * 创建角色
     * @param role
     * @return
     */
    Role createRole(Role role);

    /**
     * 删除角色
     * @param roleId
     */
    void deleteRole(Long roleId);

    /**
     * 关联角色和权限
     * @param roleId
     * @param permissionIds
     */
    void correlationPermissions(Long roleId, Long... permissionIds);

    /**
     * 解除角色和权限的关联
     * @param roleId
     * @param permissionIds
     */
    void uncorrelationPermissions(Long roleId, Long... permissionIds);

    /**
     * 关联是否存在
     * @param roleId
     * @param permissionId
     * @return
     */
    boolean exists(Long roleId, Long permissionId);

}
