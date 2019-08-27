package chapter5.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

/**
 * @Author: yangke
 * @Date: 2019/8/23
 */
public class MyRealm2 extends AuthorizingRealm {
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //用户名及salt1
        String username = "liu";
        String salt2 = "fe9ff7a67237e6062b594bd5602cdb68";
        //加密后的密码
        String password = "a38132fc3de7532da3bb9037fae996f8";
        SimpleAuthenticationInfo ai = new SimpleAuthenticationInfo(username, password, getName());
        //盐是用户名+随机数
        ai.setCredentialsSalt(ByteSource.Util.bytes(username+salt2));
        return ai;
    }
}
