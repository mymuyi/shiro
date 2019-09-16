package chapter8.filter;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.web.filter.PathMatchingFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: yangke
 * @Date: 2019/9/10
 */
public class FormLoginFilter extends PathMatchingFilter {

    private String loginUrl = "/login.jsp";
    private String successUrl = "/";

    @Override
    protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        if (SecurityUtils.getSubject().isAuthenticated()) {
            return true;
        }
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        if (isLoginRequest(req)) {
            if ("post".equalsIgnoreCase(req.getMethod())) {
                boolean loginSuccess = login(req);
                if (loginSuccess) {
                    redirectToSuccessUrl(req, resp);
                }
            }
            return true;
        } else {
            saveRequestAndRedirectToLogin(req, resp);
            return false;
        }
    }

    /**
     * 登录
     *
     * @param request
     * @return
     */
    private boolean login(HttpServletRequest request) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        try {
            SecurityUtils.getSubject().login(new UsernamePasswordToken(username, password));
        } catch (Exception e) {
            request.setAttribute("shiroLoginFailure", e.getClass());
            return false;
        }
        return true;
    }

    /**
     * 重定向到登录成功页面
     * @param req
     * @param resp
     * @return
     * @throws IOException
     */
    private boolean redirectToSuccessUrl(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        WebUtils.redirectToSavedRequest(req, resp, successUrl);
        return false;
    }

    /**
     * 保存当前地址并重定向到登录页面
     * @param req
     * @param resp
     * @throws IOException
     */
    private void saveRequestAndRedirectToLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        WebUtils.saveRequest(req);
        WebUtils.issueRedirect(req, resp, loginUrl);
    }

    /**
     * 是否是登录请求
     *
     * @param req
     * @return
     */
    private boolean isLoginRequest(HttpServletRequest req) {
        return pathsMatch(loginUrl, WebUtils.getPathWithinApplication(req));
    }
}
