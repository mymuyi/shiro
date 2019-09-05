package chapter6;

import chapter6.entity.Permission;
import chapter6.entity.Role;
import chapter6.entity.User;
import chapter6.service.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.apache.shiro.util.ThreadContext;
import org.junit.After;
import org.junit.Before;

/**
 * @Author: yangke
 * @Date: 2019/9/3
 */
public abstract class BaseTest {

    protected UserService userService = new UserServiceImpl();
    protected RoleService roleService = new RoleServiceImpl();
    protected PermissionService permissionService = new PermissionServiceImpl();

    protected User u1;
    protected User u2;
    protected User u3;
    protected User u4;
    protected Role r1;
    protected Role r2;
    protected Permission p1;
    protected Permission p2;
    protected Permission p3;

    protected String password = "123";

    @Before
    public void setUp() {
        // 清除数据
        JdbcTemplateUtils.jdbcTemplate().update("delete from sys_role");
        JdbcTemplateUtils.jdbcTemplate().update("delete from sys_user");
        JdbcTemplateUtils.jdbcTemplate().update("delete from sys_permission");
        JdbcTemplateUtils.jdbcTemplate().update("delete from sys_user_role");
        JdbcTemplateUtils.jdbcTemplate().update("delete from sys_role_permission");

        // 新增权限
        p1 = new Permission("user:create", "用户模块新增", Boolean.TRUE);
        p2 = new Permission("user:update", "用户模块修改", Boolean.TRUE);
        p3 = new Permission("menu:create", "菜单模块新增", Boolean.TRUE);
        permissionService.createPermission(p1);
        permissionService.createPermission(p2);
        permissionService.createPermission(p3);

        // 新增角色
        r1 = new Role("admin", "管理员", Boolean.TRUE);
        r2 = new Role("user", "用户管理员", Boolean.TRUE);
        roleService.createRole(r1);
        roleService.createRole(r2);

        // 关联角色-权限
        roleService.correlationPermissions(r1.getId(), p1.getId());
        roleService.correlationPermissions(r1.getId(), p2.getId());
        roleService.correlationPermissions(r1.getId(), p3.getId());

        roleService.correlationPermissions(r2.getId(), p1.getId());
        roleService.correlationPermissions(r2.getId(), p2.getId());

        // 新增用户
        u1 = new User("zhang", password);
        u2 = new User("li", password);
        u3 = new User("wu", password);
        u4 = new User("wang", password);
        u4.setLocked(Boolean.TRUE);
        userService.createUser(u1);
        userService.createUser(u2);
        userService.createUser(u3);
        userService.createUser(u4);
        // 关联用户-角色
        userService.correlationRoles(u1.getId(), r1.getId());


    }

    protected void login(String configFile, String username, String password) {
        //1、获取SecurityManager工厂，此处使用Ini配置文件初始化SecurityManager
        Factory<SecurityManager> factory =
                new IniSecurityManagerFactory(configFile);

        //2、得到SecurityManager实例 并绑定给SecurityUtils
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);

        //3、得到Subject及创建用户名/密码身份验证Token（即用户身份/凭证）
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);

        subject.login(token);
    }

    public Subject subject() {
        return SecurityUtils.getSubject();
    }

    @After
    public void tearDown() throws Exception {
        //退出时请解除绑定Subject到线程 否则对下次测试造成影响
        ThreadContext.unbindSubject();
    }

}
