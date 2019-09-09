package chapter6.credentials;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: yangke
 * @Date: 2019/8/30
 *
 */
public class RetryLimitHashedCredentialsMatcher extends HashedCredentialsMatcher {

    public static final ExcessiveAttemptsException EXCESSIVE_ATTEMPTS_EXCEPTION = new ExcessiveAttemptsException();
    private Ehcache passwordRetryCache;

    public RetryLimitHashedCredentialsMatcher() {
        CacheManager cacheManager = CacheManager.newInstance
                (CacheManager.class.getClassLoader().getResource("ehcache.xml"));
        passwordRetryCache = cacheManager.getCache("passwordRetryCache");
    }

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        String userName = (String) token.getPrincipal();

        Element element = passwordRetryCache.get(userName);
        if (element == null) {
            element = new Element(userName, new AtomicInteger(0));
            passwordRetryCache.put(element);
        }

        AtomicInteger retryCount = (AtomicInteger)element.getObjectValue();
        // 重试次数大于5，抛出异常
        if (retryCount.incrementAndGet() > 5) {
            throw EXCESSIVE_ATTEMPTS_EXCEPTION;
        }

        boolean matches = super.doCredentialsMatch(token, info);

        // 匹配成功
        if (matches) {
            passwordRetryCache.remove(userName);
        }

        return matches;
    }
}
