package chapter3.permission;

import com.alibaba.druid.util.StringUtils;
import org.apache.shiro.authz.Permission;

/**
 * 规则
 * +资源字符串+权限位+实例ID
 * <p>
 * 以+开头 中间通过+分割
 * <p>
 * 权限：
 * 0 表示所有权限
 * 1 新增 0001
 * 2 修改 0010
 * 4 删除 0100
 * 8 查看 1000
 * <p>
 * 如 +user+10(1010) 表示对资源user拥有修改/查看权限
 * <p>
 * 不考虑一些异常情况
 *
 * @Author: yangke
 * @Date: 2019/8/22
 */
public class BitPermission implements Permission {

    private String resourceIdentify;
    private int permissionBit;
    private String instanceId;

    public BitPermission(String permissionString) {
        String[] array = permissionString.split("\\+");

        if (array.length > 1) {
            this.resourceIdentify = array[1];
        }

        if (StringUtils.isEmpty(this.resourceIdentify)) {
            this.resourceIdentify = "*";
        }

        if (array.length > 2) {
            this.permissionBit = Integer.valueOf(array[2]);
        }

        if (array.length > 3) {
            this.instanceId = array[3];
        }

        if (StringUtils.isEmpty(this.instanceId)) {
            this.instanceId = "*";
        }
    }

    /**
     * 权限匹配，代码是复制的，还没看懂是怎么匹配的
     * @param p
     * @return
     */
    @Override
    public boolean implies(Permission p) {
        if (!(p instanceof BitPermission)) {
            return false;
        }

        BitPermission other = (BitPermission) p;

        if (!("*".equals(this.resourceIdentify) || this.resourceIdentify.equals(other.resourceIdentify))) {
            return false;
        }

        if (!(this.permissionBit == 0 || (this.permissionBit & other.permissionBit) != 0)) {
            return false;
        }

        if (!("*".equals(this.instanceId) || this.instanceId.equals(other.instanceId))) {
            return false;
        }
        return true;
    }
}
