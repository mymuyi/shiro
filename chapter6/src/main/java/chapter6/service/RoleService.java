package chapter6.service;

import chapter6.entity.Role;

/**
 * @Author: yangke
 * @Date: 2019/8/30
 */
public interface RoleService {

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
     * 添加角色-权限之间关系
     * @param roleId
     * @param permissionIds
     */
    void correlationPermissions(Long roleId, Long... permissionIds);

    /**
     * 移除角色-权限之间关系
     * @param roleId
     * @param permissionIds
     */
    void uncorrelationPermissions(Long roleId, Long... permissionIds);

}
