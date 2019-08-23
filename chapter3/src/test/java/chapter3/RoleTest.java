package chapter3;

import org.apache.shiro.authz.UnauthorizedException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * @Author: yangke
 * @Date: 2019/8/22
 * 基于角色的访问控制（隐式角色）
 */
public class RoleTest extends BaseTest {

    @Test
    public void testHasRole() {
        login("classpath:shiro-role.ini", "zhang", "123");

        // 拥有角色:role1
        Assert.assertTrue(subject().hasRole("role1"));

        // 没有角色:role3
        Assert.assertFalse(subject().hasRole("role3"));

        // 拥有角色:role1，role2
        Assert.assertTrue(subject().hasAllRoles(Arrays.asList("role1", "role2")));

        // 拥有角色:role1,role2,!role3
        boolean[] result = subject().hasRoles(Arrays.asList("role1", "role2", "role3"));

        Assert.assertTrue(result[0]);

        Assert.assertTrue(result[1]);

        Assert.assertFalse(result[2]);
    }

    @Test(expected = UnauthorizedException.class)
    public void testCheckRole() {
        login("classpath:shiro-role.ini", "zhang", "123");

        // 断言拥有角色：role1
        subject().checkRole("role1");

        // 断言拥有角色：role1,role3, 失败抛出异常
        subject().checkRoles("role1", "role3");
    }

}
