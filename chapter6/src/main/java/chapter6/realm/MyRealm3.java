package chapter6.realm;

import chapter6.entity.User;
import org.apache.shiro.authc.*;
import org.apache.shiro.realm.Realm;

/**
 * @Author: yangke
 * @Date: 2019/9/3
 */
public class MyRealm3 implements Realm {

    @Override
    public String getName() {
        //realm name 为 “c”
        return "c";
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof UsernamePasswordToken;
    }

    @Override
    public AuthenticationInfo getAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        User user = new User("zhang", "123");
        return new SimpleAuthenticationInfo(
                user,
                "123",
                getName()
        );
    }
}
