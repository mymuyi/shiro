package chapter6.realm;

import org.apache.shiro.authc.*;
import org.apache.shiro.realm.Realm;

/**
 * @Author: yangke
 * @Date: 2019/9/3
 */
public class MyRealm2 implements Realm {

    @Override
    public String getName() {
        //realm name 为 “b”
        return "b";
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof UsernamePasswordToken;
    }

    @Override
    public AuthenticationInfo getAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        return new SimpleAuthenticationInfo(
                "zhang",
                "123",
                getName()
        );
    }
}
