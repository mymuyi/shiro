package chapter6.realm;

import chapter6.BaseTest;
import chapter6.entity.User;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.Set;


/**
 * @Author: yangke
 * @Date: 2019/9/5
 */
public class PrincipalCollectionTest extends BaseTest {

    @Test
    public void test() {
        //因为Realm里没有进行验证，所以相当于每个Realm都身份验证成功了
        login("classpath:shiro-multirealm.ini", "zhang", "123");
        Subject subject = subject();

        // 获取 Primary Principal(第一个)
        Object primaryPrincipal1 = subject.getPrincipal();
        PrincipalCollection principalCollection = subject.getPrincipals();
        Object primaryPrincipal2 = principalCollection.getPrimaryPrincipal();

        System.out.println(primaryPrincipal1);
        System.out.println(primaryPrincipal2);
        System.out.println(primaryPrincipal1 == primaryPrincipal2);

        //但是因为多个Realm都返回了Principal，所以此处到底是哪个是不确定的
        Assert.assertEquals(primaryPrincipal1, primaryPrincipal2);

        // 返回 a b c
        Set<String> realmNames = principalCollection.getRealmNames();
        System.out.println(realmNames);

        //因为返回的凭据都是 zhang，所以排重了
        Set<Object> principals = principalCollection.asSet();
        System.out.println(principals);
        // asList 也是只有一个，因为会先转换为 Set，在转换成 List
        List<Object> principals1 = principalCollection.asList();
        System.out.println(principals1);

        // 根据 Realm 名字获取
        Collection<User> users = principalCollection.fromRealm("b");
        System.out.println(users);
    }


}
