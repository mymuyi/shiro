package chapter2;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.apache.shiro.util.ThreadContext;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

/**
 * @Author: yangke
 * @Date: 2019/8/13
 *
 * 身份认证器测试
 */
public class AuthenticatorTest {

    /**
     * 所有 Realm 验证成功，返回全部身份信息
     */
    @Test
    public void testAllSuccessfulStrategyWithSuccess() {
        login("classpath:shiro-authenticator-all-success.ini");
        Subject subject = SecurityUtils.getSubject();

        // 得到一个身份集合，包含了 Realm 验证成功的身份信息
        PrincipalCollection principalCollection = subject.getPrincipals();
        // 指定的 Realm 是 MyRealm1(zhang/123),MyRealm3(zhang/123)。两个都验证成功，身份信息全部返回
        Assert.assertEquals(2, principalCollection.asList().size());
    }

    /**
     * Realm 没有全部验证成功，失败测试
     */
    @Test(expected = UnknownAccountException.class)
    public void testAllSuccessfulStrategyWithFail() {
        // 指定的 Realm 是 MyRealm1(zhang/123),MyRealm2(wang/123)。MyRealm2 验证失败，抛出异常
        login("classpath:shiro-authenticator-all-fail.ini");
    }

    /**
     * 至少一个 Realm 验证成功，返回所有的成功的身份信息
     */
    @Test
    public void testAtLeastOneSuccessfulStrategyWithSuccess() {
        login("classpath:shiro-authenticator-atLeastOne-success.ini");
        Subject subject = SecurityUtils.getSubject();

        //得到一个身份集合，其包含了Realm验证成功的身份信息
        PrincipalCollection principalCollection = subject.getPrincipals();
        // MyRealm1,Myrealm3验证成功，MyRealm2验证失败，返回MyRealm1,Myrealm3验证成功的身份信息
        Assert.assertEquals(2, principalCollection.asList().size());
    }

    /**
     * 至少一个 Realm 验证成功，如果有多个，返回第一个
     */
    @Test
    public void testFirstOneSuccessfulStrategyWithSuccess() {
        login("classpath:shiro-authenticator-first-success.ini");
        Subject subject = SecurityUtils.getSubject();

        //得到一个身份集合，其包含了第一个Realm验证成功的身份信息
        PrincipalCollection principalCollection = subject.getPrincipals();
        // MyRealm1,Myrealm3验证成功，MyRealm1 在前，返回 MyRealm1 验证成功的认证信息
        Assert.assertEquals(1, principalCollection.asList().size());
    }

    /**
     * 只能有一个 Realm 验证成功，成功测试
     */
    @Test
    public void testOnlyOneStrategyWithSuccess() {
        login("classpath:shiro-authenticator-onlyone-success.ini");
        Subject subject = SecurityUtils.getSubject();

        //myRealm1 验证成功，myRealm2 验证失败
        PrincipalCollection principalCollection = subject.getPrincipals();
        junit.framework.Assert.assertEquals(1, principalCollection.asList().size());
    }

    /**
     * 只能有一个 Realm 验证成功，失败测试
     */
    @Test(expected = AuthenticationException.class)
    public void testOnlyOneStrategyWithFail() {
        //myRealm1 和 myRealm3验证成功，超过一个
        login("classpath:shiro-authenticator-onlyone-fail.ini");
    }

    /**
     * 至少两个 Realm 验证成功，成功测试
     */
    @Test
    public void testAtLeastTwoStrategyWithSuccess() {
        login("classpath:shiro-authenticator-atLeastTwo-success.ini");
        Subject subject = SecurityUtils.getSubject();

        //得到一个身份集合，因为 myRealm1 和 myRealm4 返回的身份一样所以输出时只返回一个
        PrincipalCollection principalCollection = subject.getPrincipals();
        Assert.assertEquals(1, principalCollection.asList().size());
        // realmName 还是有两个
        Assert.assertEquals(2, principalCollection.getRealmNames().size());
    }

    /**
     * 至少两个 Realm 验证成功，失败测试
     */
    @Test(expected = AuthenticationException.class)
    public void testAtLeastTwoStrategyWithFail() {
        // 指定的 Realm 是 MyRealm1(zhang/123),MyRealm2(wang/123)。只有 MyRealm1 一个验证成功，抛出异常
        login("classpath:shiro-authenticator-atLeastTwo-fail.ini");
    }


    private void login(String configFile) {
        //1、获取SecurityManager工厂，此处使用Ini配置文件初始化SecurityManager
        Factory<SecurityManager> factory =
                new IniSecurityManagerFactory(configFile);

        //2、得到SecurityManager实例 并绑定给SecurityUtils
        org.apache.shiro.mgt.SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);

        //3、得到Subject及创建用户名/密码身份验证Token（即用户身份/凭证）
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("zhang", "123");

        subject.login(token);
    }

    @After
    public void tearDown() {
        //退出时请解除绑定Subject到线程 否则对下次测试造成影响
        ThreadContext.unbindSubject();
    }

}
