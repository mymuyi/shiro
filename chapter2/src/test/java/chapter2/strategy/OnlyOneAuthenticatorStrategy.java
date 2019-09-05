package chapter2.strategy;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.pam.AbstractAuthenticationStrategy;
import org.apache.shiro.realm.Realm;

import java.util.Collection;

/**
 * @Author: yangke
 * @Date: 2019/8/13
 *
 * 自定义验证策略，只能有一个 Realm 验证成功
 */
public class OnlyOneAuthenticatorStrategy extends AbstractAuthenticationStrategy {


    public OnlyOneAuthenticatorStrategy() {
        super();
    }

    /**
     * 在所有 Realm 验证之前调用
     */
    @Override
    public AuthenticationInfo beforeAllAttempts(Collection<? extends Realm> realms, AuthenticationToken token) throws AuthenticationException {
        // 返回一个权限的认证信息
        return new SimpleAuthenticationInfo();
    }

    /**
     * 在每个 Realm 验证之前调用
     */
    @Override
    public AuthenticationInfo beforeAttempt(Realm realm, AuthenticationToken token, AuthenticationInfo aggregate) throws AuthenticationException {
        // 返回之前合并的
        return aggregate;
    }

    /**
     * 在每个 Realm 验证之后调用
     */
    @Override
    public AuthenticationInfo afterAttempt(Realm realm, AuthenticationToken token, AuthenticationInfo singleRealmInfo, AuthenticationInfo aggregateInfo, Throwable t) throws AuthenticationException {
        AuthenticationInfo info;
        if (singleRealmInfo == null) {
            info = aggregateInfo;
        } else {
            if (aggregateInfo ==null) {
                info = singleRealmInfo;
            } else {
                info = merge(singleRealmInfo, aggregateInfo);
                if (info.getPrincipals().getRealmNames().size() > 1) {
                    throw new AuthenticationException("Authentication token of type [" + token.getClass() + "] " +
                            "could not be authenticated by any configured realms.  Please ensure that only one realm can " +
                            "authenticate these tokens.");
                }
            }
        }
        return info;
    }

    @Override
    protected AuthenticationInfo merge(AuthenticationInfo info, AuthenticationInfo aggregate) {
        return super.merge(info, aggregate);
    }

    /**
     * 在所有 Realm 验证之后调用
     */
    @Override
    public AuthenticationInfo afterAllAttempts(AuthenticationToken token, AuthenticationInfo aggregate) throws AuthenticationException {
        return aggregate;
    }
}
