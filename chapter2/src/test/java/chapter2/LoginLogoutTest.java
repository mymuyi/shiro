package chapter2;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.Assert;
import org.junit.Test;


/**
 * @Author: yangke
 * @Date: 2019/8/13
 *
 * 登录、注销测试
 */
public class LoginLogoutTest {

    /**
     * 不使用 Realm
     */
    @Test
    public void testHelloWorld() {
        // 获取 SecurityManager 工厂
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");

        // 得到 SecurityManager 实例，绑定到 SecurityUtils
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);

        // 得到 Subject，创建用户名/密码身份验证token
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("zhang", "123");

        try {
            // 登录
            subject.login(token);
        } catch (Exception e) {
            // 登录失败
       }

        // 断言登录成功
        Assert.assertTrue(subject.isAuthenticated());

        // 注销
        subject.logout();
    }

    /**
     * 使用自定义 Realm
     */
    @Test
    public void testCustomRealm() {

        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro-realm.ini");

        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);

        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken("zhang","456");

        try {
            subject.login(token);
        } catch (Exception e) {

        }

        // 断言登录失败
        Assert.assertFalse(subject.isAuthenticated());
    }

    /**
     * 多个 Realm
     */
    @Test
    public void testCustomMultRealm() {

        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro-multi-realm.ini");

        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);

        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken("wang","123");

        try {
            subject.login(token);
        } catch (Exception e) {

        }

        Assert.assertTrue(subject.isAuthenticated());

        subject.logout();
    }

    /**
     * JDBC Realm
     */
    @Test
    public void testJDBCRealm() {

        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro-jdbc-realm.ini");

        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);

        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken("zhang","123");

        try {
            subject.login(token);
        } catch (Exception e) {

        }

        Assert.assertTrue(subject.isAuthenticated());

        subject.logout();
    }

}

