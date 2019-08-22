package chapter2.realm;

import org.apache.shiro.authc.*;
import org.apache.shiro.realm.Realm;

/**
 * @Author: yangke
 * @Date: 2019/8/13
 */
public class MyRealm4 implements Realm {
    @Override
    public String getName() {
        return "myRealm4";
    }

    @Override
    public boolean supports(AuthenticationToken authenticationToken) {
        // 仅支持 UsernamePasswordToken 类型的 Token
        return authenticationToken instanceof UsernamePasswordToken;
    }

    @Override
    public AuthenticationInfo getAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        // 得到用户名
        String username = (String) authenticationToken.getPrincipal();
        // 得到密码
        String password = new String((char[])authenticationToken.getCredentials());

        if (!"zhang".equals(username)) {
            // 用户名错误
            throw new UnknownAccountException();
        }
        if (!"123".equals(password)) {
            // 密码错误
            throw new IncorrectCredentialsException();
        }
        // 验证成功
        return new SimpleAuthenticationInfo(username, password, getName());
    }
}
