package chapter6.service;

import chapter6.entity.Permission;

/**
 * @Author: yangke
 * @Date: 2019/8/30
 */
public interface PermissionService {

    /**
     * 创建角色
     * @param permission
     * @return
     */
    Permission createPermission(Permission permission);

    /**
     * 删除角色
     * @param permissionId
     */
    void deletePermission(Long permissionId);

}
