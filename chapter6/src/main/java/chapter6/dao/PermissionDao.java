package chapter6.dao;

import chapter6.entity.Permission;

/**
 * @Author: yangke
 * @Date: 2019/8/28
 */
public interface PermissionDao {

    /**
     * 创建权限
     * @param permission
     * @return
     */
    Permission createPermission(Permission permission);

    /**
     * 删除权限
     * @param permissionId
     */
    void deletePermission(Long permissionId);

}
