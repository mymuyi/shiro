package chapter8.env;

import org.apache.shiro.util.ClassUtils;
import org.apache.shiro.web.env.IniWebEnvironment;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.filter.authz.RolesAuthorizationFilter;
import org.apache.shiro.web.filter.mgt.DefaultFilter;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.FilterChainResolver;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;

import javax.servlet.Filter;

/**
 * @Author: yangke
 * @Date: 2019/9/11
 */
public class MyIniWebEnvironment extends IniWebEnvironment {

    /**
     * 覆盖默认的 FilterChainResolver，配置文件中的 [url] 部分失效，需要自己实现
     * @return
     */
    @Override
    protected FilterChainResolver createFilterChainResolver() {
        PathMatchingFilterChainResolver filterChainResolver =
                new PathMatchingFilterChainResolver();
        DefaultFilterChainManager filterChainManager = new DefaultFilterChainManager();
        // 注册 filter
        for (DefaultFilter filter : DefaultFilter.values()) {
            filterChainManager.addFilter(filter.name(), (Filter) ClassUtils.newInstance(filter.getFilterClass()));
        }
        // 注册 URL-Filter 映射关系、
        filterChainManager.addToChain("/login.jsp", "authc");
        filterChainManager.addToChain("/unauthorized.jsp", "anon");
        filterChainManager.addToChain("/**", "authc");
        filterChainManager.addToChain("/**", "roles", "admin");

        // 设置 Filter 的属性
        FormAuthenticationFilter authcFilter =
                (FormAuthenticationFilter) filterChainManager.getFilter("authc");
        authcFilter.setLoginUrl("/login.jsp");
        RolesAuthorizationFilter rolesFilter =
                (RolesAuthorizationFilter) filterChainManager.getFilter("roles");
        rolesFilter.setUnauthorizedUrl("/unauthorized.jsp");

        filterChainResolver.setFilterChainManager(filterChainManager);
        return filterChainResolver;
    }

}
